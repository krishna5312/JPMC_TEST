package com.message.assess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ProcessMessage {

	private static final String SPLIT_OPERAND = "\\s";
	private static final String MULTIPLY = "multiply";
	private static final String SUBTRACT = "subtract";
	private static final String ADD = "add";
	private List<Message> messageList = new ArrayList<Message>();
	private static final Logger LOGGER =  LoggerFactory.getLogger(ProcessMessage.class);

	/**
	 * @param fileName - file that needs to be read
	 * @throws IOException
	 */
	public void processFile(String fileName) throws IOException {
		File file = new File(getClass().getClassLoader().getResource(fileName).getFile());
		FileReader in = new FileReader(file);
		BufferedReader br = new BufferedReader(in);
		String message;
		int lineNum = 0;
		while ((message = br.readLine()) != null && lineNum < 50) {
			int type = determineMessageType(message);
			processMessage(type, message);
			lineNum++;
			if (lineNum % 10 == 0) {
				printTotalSalesValues(messageList);
			}
		}

		if (lineNum >= 50) {
			br.close();
			LOGGER.error("Will not accept any more messages. Message threshold reached");
		}
	}

	private void processMessage(int type, String message) {
		String[] elements = message.split(SPLIT_OPERAND);
		Message msg = new Message();
		if (type == 1) {
			msg.setSale(1);
			msg.setItemName(elements[0] + "s");
			msg.setPrice(getTrimmedPrice(elements[1]));
			messageList.add(msg);
		} else if (type == 2) {
				msg.setSale(Integer.parseInt(elements[0]));
				msg.setItemName(elements[2]);
				msg.setPrice(getTrimmedPrice(elements[3]));
				messageList.add(msg);

		} else if (type == 3) {
			String operation = elements[0];
			String product = elements[2];
			double price = getTrimmedPrice(elements[1]);
			List<Message> filteredMsg = messageList.stream().filter(msgs -> msgs.getItemName().equals(product))
					.collect(Collectors.toList());
			performOperation(operation, price, filteredMsg);
		} else {
			LOGGER.error("Invalid Message");
		}

	}

	private void performOperation(String operation, double price, List<Message> filteredMsg) {
		if (operation.equalsIgnoreCase(ADD)) {
			filteredMsg.forEach(msgs -> msgs.setPrice(msgs.getPrice() + price));
		} else if (operation.equalsIgnoreCase(SUBTRACT)) {
			filteredMsg.forEach(msgs -> msgs.setPrice(msgs.getPrice() - price));
		} else if (operation.equalsIgnoreCase(MULTIPLY)) {
			filteredMsg.forEach(msgs -> msgs.setPrice(msgs.getPrice() * price));
		} else {
			LOGGER.error("Invalid Operation provided");
		}
	}

	private void printTotalSalesValues(List<Message> messageList) {
		Map<String, SalesAggregate> calcMap = new HashMap<>();
		messageList.forEach(msg -> {
			SalesAggregate sa = new SalesAggregate();
			long saleValueInMsg = msg.getSale();
			String item = msg.getItemName();
			double totalValue = msg.getPrice() * saleValueInMsg;
			if (!calcMap.containsKey(item)) {
				sa.setTotalPrice(totalValue);
				sa.setTotalSales(saleValueInMsg);
				calcMap.put(item, sa);
			} else {
				SalesAggregate valueTillNow = calcMap.get(item);
				valueTillNow.setTotalPrice(valueTillNow.getTotalPrice() + totalValue);
				valueTillNow.setTotalSales(valueTillNow.getTotalSales() + saleValueInMsg);
				calcMap.put(item, valueTillNow);
			}
		});
		calcMap.entrySet().forEach(mapEntry -> {
			LOGGER.info("Product --- {} --- No of Sales and Total Value {}",mapEntry.getKey(), mapEntry.getValue());
		});

	}

	private double getTrimmedPrice(String originalPrice) {
		return Double.parseDouble(originalPrice.substring(0, originalPrice.length() - 1));
	}

	private int determineMessageType(String message) {
		if (Character.isDigit(message.charAt(0))) {
			return 2;
		} else if (message.toLowerCase().startsWith(ADD) || message.toLowerCase().startsWith(SUBTRACT)
				|| message.toLowerCase().startsWith(MULTIPLY)) {
			return 3;
		} else if (message.split(SPLIT_OPERAND).length == 2) {
			return 1;
		}
		return 0;
	}

}
