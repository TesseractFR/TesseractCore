package onl.tesseract.core.sit

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Bisected
import org.bukkit.block.data.Directional
import org.bukkit.block.data.type.Campfire
import org.bukkit.block.data.type.Slab
import org.bukkit.block.data.type.Stairs
import org.bukkit.entity.Player

enum class SeatBlock {
    Slabs() {
        override fun matches(block: Block): Boolean {
            return "SLAB" in block.type.toString() && isTopBlockFree(block)
                    && (block.blockData as Slab).type == Slab.Type.BOTTOM
        }
    },
    Campfire() {
        override fun matches(block: Block): Boolean {
            return block.type == Material.CAMPFIRE && isTopBlockFree(block) && !((block.blockData as Campfire).isLit)
        }
    },
    Stairs() {
        override fun matches(block: Block): Boolean {
            return "STAIRS" in block.type.toString() && isTopBlockFree(block)
                    && (block.blockData as Stairs).half == Bisected.Half.BOTTOM
        }

        override fun getSitRotation(block: Block, player: Player): Float {
            val data = block.blockData as Directional
            return when (data.facing) {
                BlockFace.NORTH -> 0f
                BlockFace.SOUTH -> 180f
                BlockFace.EAST -> 90f
                BlockFace.WEST -> -90f
                else -> super.getSitRotation(block, player)
            }
        }
    },
    ;

    abstract fun matches(block: Block): Boolean

    open fun getSitLocation(block: Block): Location = block.location.add(0.5, -0.4, 0.5)

    open fun getSitRotation(block: Block, player: Player): Float = player.location.yaw

    protected fun isTopBlockFree(block: Block): Boolean {
        return !block.getRelative(BlockFace.UP).type.isSolid
    }
}