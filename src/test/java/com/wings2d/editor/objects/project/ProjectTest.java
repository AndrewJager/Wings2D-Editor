package com.wings2d.editor.objects.project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.wings2d.editor.objects.EditorSettings;

class ProjectTest {
	private File directory = new File(System.getProperty("user.dir") + "/src/test/resources");
	private static File newDirectory = new File(System.getProperty("user.dir") + "/src/test/resources/test");
	private EditorSettings settings;
	
	@BeforeAll
	static void setUpAll() throws Exception {
		newDirectory.mkdir();
	}
	@BeforeEach
	void setUp() throws Exception {
		settings = new EditorSettings();
	}

	@AfterAll
	static void tearDown() throws Exception {
		String[]entries = newDirectory.list();
		for(String s: entries){
		    File currentFile = new File(newDirectory.getPath(),s);
		    currentFile.delete();
		}
		newDirectory.delete();
	}

	@Test
	void testReadTestFile() { // Read pre-existing file in test directory
		try {
			Project testProject = new Project(directory, settings);
			assertEquals("TestProject", testProject.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();}
	}

	@Test
	void testCreateNewProjectFile() { 
		try {
			Project testProject = new Project(newDirectory, true, "NewProject", settings);
			assertEquals("NewProject", testProject.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();}
	}
	@Test
	void testReadNewProjectFile() { 
		try {
			new Project(newDirectory, true, "NewProject", settings);
			Project testProject = new Project(newDirectory, settings);
			assertEquals("NewProject", testProject.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();}
	}
}
