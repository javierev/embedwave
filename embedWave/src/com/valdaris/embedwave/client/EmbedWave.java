package com.valdaris.embedwave.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EmbedWave implements EntryPoint {
	
	private TextArea htmlArea = new TextArea();
	private TextBox widthBox = new TextBox();
	private TextBox heightBox = new TextBox();
	private Button clearButton = new Button("Clear");
	private Button sendButton = new Button("Send");
	private HorizontalPanel sizePanel = new HorizontalPanel();
	private HorizontalPanel buttonsPanel = new HorizontalPanel();
	private HandlerRegistration htmlHandlerRegistration;
	private HandlerRegistration widthHandlerRegistration;
	private HandlerRegistration heightHandlerRegistration;
	
	private class HtmlAreaHandler implements ChangeHandler {

		@Override
		public void onChange(ChangeEvent event) {
			
			// Remove handlers from width and height boxes
			if (widthHandlerRegistration != null) {
				widthHandlerRegistration.removeHandler();
			}
			if (heightHandlerRegistration != null) {
				heightHandlerRegistration.removeHandler();
			}
			
			// search for width and height values on the text
			String width = searchValue("width");
			String height = searchValue("height");
			
			// change width and height boxes values
			widthBox.setText(width);
			heightBox.setText(height);
			
			// restore handlers
			widthHandlerRegistration = widthBox.addChangeHandler(new SizeBoxHandler("width"));
			heightHandlerRegistration = heightBox.addChangeHandler(new SizeBoxHandler("height"));

		}
		
		private String searchValue(String label) {
			String value = "";
			String html = htmlArea.getText().toLowerCase();
			int startIdx = html.indexOf(label.toLowerCase() + "=\"");
			startIdx = startIdx + label.length() + 2;
			if (startIdx > -1) {
				value = html.substring(startIdx, html.indexOf('"', startIdx+1));
			}
			return value;
		}
		
	}
	
	private class SizeBoxHandler implements ChangeHandler {
		
		private String label;
		
		public SizeBoxHandler(String label) {
			this.label = label.toLowerCase();
		}
		
		@Override
		public void onChange(ChangeEvent event) {
			
			// remove handler from html text area
			if (htmlHandlerRegistration != null) {
				htmlHandlerRegistration.removeHandler();
			}
			
			// change html value for the given label (width or height)
			String html = htmlArea.getText().toLowerCase();
			String newValue = ((TextBox)event.getSource()).getText();
			html = html.replaceAll(label + "=\\\"[0-9]+\\\"", label + "=\"" + newValue + "\"");
			htmlArea.setText(html);
			
			// restore handler
			htmlHandlerRegistration = htmlArea.addChangeHandler(new HtmlAreaHandler());
		}
		
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		Window.setTitle("Embed HTML code on Google Wave");
		RootPanel mainPanel = RootPanel.get("content");
		
		//UI
		htmlArea.setSize("500", "300");
		mainPanel.add(htmlArea);
		sizePanel.add(widthBox);
		sizePanel.add(heightBox);
		mainPanel.add(sizePanel);
		buttonsPanel.add(clearButton);
		buttonsPanel.add(sendButton);
		mainPanel.add(buttonsPanel);
		
		//Handlers
		htmlHandlerRegistration = htmlArea.addChangeHandler(new HtmlAreaHandler());
		widthHandlerRegistration = widthBox.addChangeHandler(new SizeBoxHandler("width"));
		heightHandlerRegistration = heightBox.addChangeHandler(new SizeBoxHandler("height"));
		
		clearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// Remove handlers
				if (htmlHandlerRegistration != null) {
					htmlHandlerRegistration.removeHandler();
				}
				if (widthHandlerRegistration != null) {
					widthHandlerRegistration.removeHandler();
				}
				if (heightHandlerRegistration != null) {
					heightHandlerRegistration.removeHandler();
				}
				
				// Clear all text areas and boxes
				htmlArea.setText("");
				widthBox.setText("");
				heightBox.setText("");
				
				// Restore handlers
				htmlHandlerRegistration = htmlArea.addChangeHandler(new HtmlAreaHandler());
				widthHandlerRegistration = widthBox.addChangeHandler(new SizeBoxHandler("width"));
				heightHandlerRegistration = heightBox.addChangeHandler(new SizeBoxHandler("height"));
			}
			
		});
		
		sendButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				send();
			}
			
		});
		
		// focus
		htmlArea.setFocus(true);
		
	}
	
	private void send() {
		RootPanel mainPanel = RootPanel.get("content");
		mainPanel.clear();
		mainPanel.add(new HTML("Enviado (mentira)"));
	}
}
