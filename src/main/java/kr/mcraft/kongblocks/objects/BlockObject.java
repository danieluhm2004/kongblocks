package main.java.kr.mcraft.kongblocks.objects;

import java.io.Serializable;

import org.bukkit.block.Block;

public class BlockObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6753463614011275277L;
	
	private String world;
	private int X, Y, Z;

	public BlockObject(Block block) {
		world = block.getWorld().getUID().toString();
		X = block.getX();
		Y = block.getY();
		Z = block.getZ();
	}

	public boolean equals(Object object) {
		BlockObject block = (BlockObject) object;
		if (block.world.equals(world) 
				&& block.X == X 
				&& block.Y == Y 
				&& block.Z == Z) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		return world.hashCode() + X + Y + Z;
	}

	public String toString() {
		return "(" + world + ", " + X + ", " + Y + ", " + Z + ")";
	}
}
