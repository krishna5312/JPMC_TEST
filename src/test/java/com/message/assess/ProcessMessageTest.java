package com.message.assess;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.read.ListAppender;

/**
 * @author krishnakrovvidi
 * 
 * Test class for ProcessMessage.java
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ProcessMessageTest {

	@Mock
	private ListAppender<ILoggingEvent> listAppender;
	private String fileName;
	private ClassLoader classLoader;
	private File file;
	private Logger root;
	@Mock
	private Appender<ILoggingEvent> appender;
	@InjectMocks
	private ProcessMessage pms;

	@Before
	public void setup() {
		classLoader = getClass().getClassLoader();
		root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		MockitoAnnotations.initMocks(this);
		// when(appender.getName()).thenReturn("MOCK");
		// when(appender.isStarted()).thenReturn(true);
		root.addAppender(appender);
		appender.start();

	}

	@After
	public void detach() {
		appender.stop();
		root.detachAppender(appender);
		
	}
	
	/**
	 * method to test the message processing app when 10 messages were sent
	 */
	@Test
	public void testProcessFile() {

		try {
			fileName = "proper_sample.txt";
			file = new File(classLoader.getResource(fileName).getFile());
			pms.processFile(fileName);
			verify(appender, times(1)).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
				@Override
				public boolean matches(ILoggingEvent argument) {
					return (argument.getFormattedMessage()
							.equals("Product --- oranges --- No of Sales and Total Value 1  20.0p"));
				}
			}));
			verify(appender, times(1)).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
				@Override
				public boolean matches(ILoggingEvent argument) {
					return (argument.getFormattedMessage()
							.equals("Product --- bananas --- No of Sales and Total Value 4  100.0p"));
				}
			}));
			verify(appender, times(1)).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
				@Override
				public boolean matches(ILoggingEvent argument) {
					return (argument.getFormattedMessage()
							.equals("Product --- apples --- No of Sales and Total Value 80  4000.0p"));
				}
			}));
			verify(appender, times(3)).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
				@Override
				public boolean matches(ILoggingEvent argument) {
					return (argument.getFormattedMessage().contains("Product"));
				}
			}));
		} catch (IOException e) {
		}
	}
	
	/**
	 * method to test the message processing app when less than 10 messages were sent
	 */
	@Test
	public void whenInputIsLessThan10Messages() {
		try {
			fileName = "sample.txt";
			file = new File(classLoader.getResource(fileName).getFile());
			pms.processFile(fileName);
			verify(appender, times(0)).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
				@Override
				public boolean matches(ILoggingEvent argument) {
					return (argument.getFormattedMessage().equals(""));
				}
			}));
		} catch (IOException e) {

		}
	}

	/**
	 * method to test the message processing app when more than 50 messages were sent
	 */
	@Test
	public void testProcessWhenMoreThan50Messages() {
		try {
			fileName = "error_sample.txt";
			file = new File(classLoader.getResource(fileName).getFile());
			pms.processFile(fileName);
			verify(appender, times(1)).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
				@Override
				public boolean matches(ILoggingEvent argument) {
					return (argument.getLevel().equals(Level.ERROR)) && (argument.getFormattedMessage().contains("Will not accept any more messages"));
				}
			}));
			
		} catch (IOException e) {

		}
	}

}
