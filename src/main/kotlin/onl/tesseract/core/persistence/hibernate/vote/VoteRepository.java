package onl.tesseract.core.persistence.hibernate.vote;

import onl.tesseract.core.TesseractCorePlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.logging.Level;

public class VoteRepository {
    public enum VotePeriod {
        DAILY("DAYOFYEAR(DATE(date)) = DAYOFYEAR(NOW()) AND YEAR(DATE(date)) = YEAR(NOW())"),
        WEEKLY("WEEKOFYEAR(DATE(date)) = WEEKOFYEAR(NOW()) AND YEAR(DATE(date)) = YEAR(NOW())"),
        MONTHLY("MONTH(DATE(date)) = MONTH(NOW()) AND YEAR(DATE(date)) = YEAR(NOW())"),
        YEARLY("YEAR(DATE(date)) = YEAR(NOW())"),
        ;
        private final String sqlCondition;

        VotePeriod(final String sqlCondition) {this.sqlCondition = sqlCondition;}

        public String getSqlCondition()
        {
            return sqlCondition;
        }
    }

    public static class GetVoteStatementBuilder {
        private String periodCondition;
        private String playerUUIDCondition;
        private String serviceCondition;

        public int build()
        {
            try
            {
                final Connection connection = TesseractCorePlugin.instance.getBddManager().getBddConnection().getConnection();

                String statementString = "SELECT count(*) FROM t_vote ";
                StringJoiner conditions = new StringJoiner(" AND ", "WHERE ", "");

                if (playerUUIDCondition != null)
                    conditions.add(playerUUIDCondition);
                if (serviceCondition != null)
                    conditions.add(serviceCondition);
                if (periodCondition != null)
                    conditions.add(periodCondition);

                final String conditionsString = conditions.length() > 0
                                                ? conditions.toString()
                                                : "";

                PreparedStatement statement = connection.prepareStatement(statementString + conditionsString);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
            catch (SQLException throwables)
            {
                TesseractCorePlugin.instance.getLogger().log(Level.SEVERE, "Failed to retrieve vote sites", throwables);
            }
            return 0;
        }

        public GetVoteStatementBuilder setPeriod(final VotePeriod period)
        {
            this.periodCondition = period.sqlCondition;
            return this;
        }

        public GetVoteStatementBuilder setPlayerUUID(final UUID playerUUID)
        {
            this.playerUUIDCondition = "player_uuid = '" + playerUUID.toString() + "'";
            return this;
        }

        public GetVoteStatementBuilder setService(final String service)
        {
            this.serviceCondition = "service_name = '" + service + "'";
            return this;
        }
    }
}
