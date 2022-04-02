package com.wings2d.editor.objects.skeleton;

public enum DrawMode {
	BONE_MOVE,
	BONE_ROTATE,
	SPRITE_MOVE,
	SPRITE_EDIT;
	
	public enum SuperDrawMode {
		BONE,
		SPRITE,
	}
	public enum MoveType {
		MOVE_BOTH,
		MOVE_X,
		MOVE_Y
	}
	
	public SuperDrawMode getSuperMode()
	{
		SuperDrawMode superMode = SuperDrawMode.BONE;
		switch(this)
		{
		case BONE_MOVE:
			superMode = SuperDrawMode.BONE;
			break;
		case BONE_ROTATE:
			superMode = SuperDrawMode.BONE;
			break;
		case SPRITE_MOVE:
			superMode = SuperDrawMode.SPRITE;
			break;
		case SPRITE_EDIT:
			superMode = SuperDrawMode.SPRITE;
			break;
		}
		return superMode;
	}
}
