package com.wings2d.editor.objects.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.skeleton.Skeleton;

class ProjectEntityFactoryTest {
	private File directory = new File(System.getProperty("user.dir") + "/src/test/resources");
	private EditorSettings settings;
	
	@BeforeEach
	void setUp() throws Exception {
		settings = new EditorSettings();
	}

	@Test
	void testIsSkeletonFile() {
		try {
			ProjectEntity entity = ProjectEntityFactory.createFromFile(new File(directory + "/testSkeleton.txt"), 
					new Project(directory, true, "TestProject", settings), settings);
			assertTrue(entity instanceof Skeleton);
		} catch (FileNotFoundException e) {e.printStackTrace();}
	}
	@Test
	void testReadSkeletonName() {
		try {
			ProjectEntity entity = ProjectEntityFactory.createFromFile(new File(directory + "/testSkeleton.txt"),
					new Project(directory, true, "TestProject", settings), settings);
			assertEquals("testSkeleton", entity.toString());
		} catch (FileNotFoundException e) {e.printStackTrace();}
	}
}
