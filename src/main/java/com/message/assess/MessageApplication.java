package com.message.assess;

import java.io.IOException;

/**
 * @author krishnakrovvidi
 * 
 * Message Application class to read the trigger the messaging app
 *
 */
public class MessageApplication {

	public static void main(String[] args) {
		ProcessMessage pm = new ProcessMessage();
		try {
			pm.processFile("messages.txt");
		} catch (IOException e) {
			System.out.println("An exception occured while accessing the file");
		}

	}

}
