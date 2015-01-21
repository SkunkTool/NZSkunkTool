package com.nedzhang.skunktool3.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nedzhang.util.XmlUtil;

public class XmlViewer extends VBox {

	
	private static class XmlViewerCell extends
			TableCell<Map<String, Object>, Object> {

		@Override
		protected void updateItem(final Object item, final boolean empty) {
			// TODO Auto-generated method stub
			super.updateItem(item, empty);

			if (item != null && item instanceof Element) {

				try {
					
					Element[] childElements = getChildElements((Element) item);
					
					Element[] elements;
					
					if (((Element) item).hasAttributes()) {
						
						elements = new Element[childElements == null ? 1 : childElements.length+1];
						elements[0] = (Element) item;
						
						for (int i=0; childElements!=null && i<childElements.length; i++) {
							elements[i+1] = childElements[i];
						}
					} else {
						elements = childElements;
					}
					
					if (elements != null && elements.length > 0) {
						createXmlViewerCell(item, elements);
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		private void createXmlViewerCell(final Object item, Element[] elements) {
			
			XmlViewer itemViewer = new XmlViewer(elements);

		
			
			final AnchorPane apane = new AnchorPane();
			
			AnchorPane.setBottomAnchor(itemViewer, 0.0);
			AnchorPane.setTopAnchor(itemViewer, 0.0);
			AnchorPane.setLeftAnchor(itemViewer, 0.0);
			AnchorPane.setRightAnchor(itemViewer, 0.0);
			
			apane.getChildren().add(itemViewer);
			
			apane.setPrefHeight(-1.0);
			apane.setPrefWidth(-1.0);
//						final TitledPane wrapper = new TitledPane();
//						wrapper.setExpanded(false);
//						wrapper.setContent(apane);
//						wrapper.setText(((Element)item).getNodeName());
			
			
			final MenuItem wizPopup = new MenuItem();
				wizPopup.setGraphic(apane);

			final MenuButton popupButton = new MenuButton(((Element)item).getNodeName());
				popupButton.getItems().setAll(
					wizPopup            
			);
			
			setGraphic(popupButton);
			
		}
	}

	private static class XmlViwerCellValueFactory
			implements
			Callback<TableColumn.CellDataFeatures<Map<String, Object>, Object>, ObservableValue<Object>> {

		private final String columnName;

		public XmlViwerCellValueFactory(final String columnName) {
			this.columnName = columnName;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ObservableValue<Object> call(
				final CellDataFeatures<Map<String, Object>, Object> data) {

			final Object value = data.getValue().get(columnName);

			return (value instanceof ObservableValue) ? (ObservableValue<Object>) value
					: new ReadOnlyObjectWrapper<Object>(value);
		}

	}

//	public static XmlViewer createXmlViwer(final Document dataDocument)
//			throws IllegalArgumentException, ParserConfigurationException,
//			SAXException, IOException {
//
//
//	}
//
//	public static XmlViewer createXmlViwer(final Element dataFragement)
//			throws IllegalArgumentException, ParserConfigurationException,
//			SAXException, IOException {
//
//		return new XmlViewer(dataFragement);
//	}

	private static Element[] getChildElements(Node node) {

		if (node != null && node.hasChildNodes()) {
			ArrayList<Element> elements = new ArrayList<>();

			NodeList childNodeList = node.getChildNodes();

			for (int i = 0; i < childNodeList.getLength(); i++) {

				if (childNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					elements.add((Element) childNodeList.item(i));
				}
			}

			if (elements != null && elements.size() > 0) {
				Element[] returnArray = new Element[elements.size()];

				elements.toArray(returnArray);

				return returnArray;
			}
		}

		return null;

	}

	public XmlViewer() throws IllegalArgumentException,
			ParserConfigurationException, SAXException, IOException {
		
//		Document sampleDoc = XmlUtil.getDocument(this.getClass().getResourceAsStream("user_list.xml"));
//		loadXmlData(sampleDoc);
		
	}

	public XmlViewer(Document dataDoc) {
		loadXmlData(dataDoc);
	}
	
	
	private XmlViewer(final Element... nodes) {
		loadXmlData(nodes);
	}
	
	
	public void loadXmlData(Document dataDoc) {
		

		final Element firstChild = XmlUtil.getRootElement(dataDoc);

		if (firstChild.hasChildNodes()) {
			this.loadXmlData(getChildElements(firstChild));
		} else {
			this.loadXmlData(firstChild);
		}
		
	}
	
	public void loadXmlData(final Element... nodes) {

		
		TableView<Map<String, Object>> table = createTable(nodes);
		
		getChildren().clear();
		
		if (table != null) {
			VBox.setVgrow(table, Priority.ALWAYS);
			getChildren().add(table);
//			getChildren().add(new Label("End of the vbox"));
		}

	}

//	private XmlViewer(TableView<Map<String, Object>> tableView) {
//		getChildren().add(tableView);
//	}

	// private static Map<String, List<Element>> processNode(
	// final Element nodeToProcess) {
	//
	// final Map<String, List<Element>> childPathMap = new HashMap<>();
	//
	// if (nodeToProcess != null && nodeToProcess.getChildNodes() != null
	// && nodeToProcess.getChildNodes().getLength() > 0) {
	//
	// for (int i = 0; i < nodeToProcess.getChildNodes().getLength(); i++) {
	// final Node childNode = nodeToProcess.getChildNodes().item(i);
	//
	// if (childNode != null
	// && childNode.getNodeType() == Node.ELEMENT_NODE) {
	// // String childNodePath = path + "/" +
	// // childNode.getNodeName();
	// List<Element> nodes;
	//
	// final String childNodeName = childNode.getNodeName();
	//
	// if (!childPathMap.containsKey(childNodeName)) {
	// nodes = new ArrayList<Element>();
	// childPathMap.put(childNodeName, nodes);
	// } else {
	// nodes = childPathMap.get(childNodeName);
	// }
	//
	// nodes.add((Element) childNode);
	//
	// // processNode(childNodePath, (Element) childNode);
	// }
	// }
	// }
	//
	// return childPathMap;
	// }

	private static TableView<Map<String, Object>> createTable(
			final Element... childElements) {

		// List of the columns of the table. It includes all the attributes of
		// child elements as well ass the child/child nodes name (prefixed with
		// "*").

		if (childElements != null && childElements.length > 0) {
			
			final ArrayList<String> columnList = new ArrayList<>();

			final ObservableList<Map<String, Object>> allData = FXCollections
					.observableArrayList();

			for (int row = 0; row < childElements.length; row++) {
				
				final Element element = childElements[row];
				
				// element.setUserData(KEY_CHILD_PATH, childPathMap, null);

				Map<String, Object> valueMap = mapAttributes(columnList,
						element);

				// TODO: Add code to read text nodes under this one

				// allData.add(valueMap);

				valueMap = mapChildNodes(columnList, element, valueMap);

				allData.add(valueMap);
			}

			final TableView<Map<String, Object>> table = new TableView<>(
					allData);

			final List<TableColumn<Map<String, Object>, Object>> columns = new ArrayList<>();

			for (final String colName : columnList) {

				final TableColumn<Map<String, Object>, Object> column = new TableColumn<>(
						colName);

				// if the column represents a childNode. Set the viewer to be
				// XmlViwerCell;
				if (colName.startsWith("*")) {
					
					column.setCellFactory(new Callback<TableColumn<Map<String, Object>, Object>, TableCell<Map<String, Object>, Object>>() {
						@Override
						public TableCell<Map<String, Object>, Object> call(
								final TableColumn<Map<String, Object>, Object> param) {
							return new XmlViewerCell();
						}
					});

				}

				column.setCellValueFactory(new XmlViwerCellValueFactory(colName));

				column.setMinWidth(10);

				columns.add(column);
			}

			table.getColumns().addAll(columns);
			
			table.getSelectionModel().setCellSelectionEnabled(true);
			table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

			return table;
		} else {

			return null;
		}

	}

	private static Map<String, Object> mapChildNodes(
			final ArrayList<String> columnList, final Element element,
			final Map<String, Object> valueMap) {

		final NodeList childNodes = element.getChildNodes();

		if (childNodes != null && childNodes.getLength() > 0) {

			for (int i = 0; i < childNodes.getLength(); i++) {

				final Node child = childNodes.item(i);

				if (child.getNodeType() == Node.ELEMENT_NODE) {
					final String name = "*" + child.getNodeName();
					valueMap.put(name, child);

					if (!columnList.contains(name)) {
						columnList.add(name);
					}
				}
			}
		}

		return valueMap;
	}

	private static Map<String, Object> mapAttributes(
			final ArrayList<String> columnList, final Element element) {

		final NamedNodeMap attributeMap = element.getAttributes();

		final Map<String, Object> valueMap = new HashMap<>();

		if (attributeMap != null && attributeMap.getLength() > 0) {

			for (int i = 0; i < attributeMap.getLength(); i++) {

				final Node attributeNode = attributeMap.item(i);

				final String attributName = attributeNode.getNodeName();

				final String attributValue = attributeNode.getNodeValue();

				valueMap.put(attributName, attributValue);

				if (!columnList.contains(attributName)) {
					columnList.add(attributName);
				}
			}
		}
		
		NodeList childNodes = element.getChildNodes();
		
		if (childNodes != null && childNodes.getLength() > 0) {
			for (int i = 0; i < childNodes.getLength(); i++) {

				Node child = childNodes.item(i);

				final String attributName = child.getNodeName();

				String attributValue = XmlUtil.getNodeValue(child);

				if (attributValue != null && attributValue.length() > 0) {
					valueMap.put(attributName, attributValue);

					if (!columnList.contains(attributName)) {
						columnList.add(attributName);
					}
				}

			}
		}
		return valueMap;
	}

	// private ObservableList<Map> generateDataInMap() {
	// int max = 10;
	// ObservableList<Map> allData = FXCollections.observableArrayList();
	// for (int i = 1; i < max; i++) {
	// Map<String, String> dataRow = new HashMap<>();
	//
	// String value1 = "A" + i;
	// String value2 = "B" + i;
	//
	// dataRow.put(Column1MapKey, value1);
	// dataRow.put(Column2MapKey, value2);
	//
	// allData.add(dataRow);
	// }
	// return allData;
	// }

	// private Label makeSelectable(String text) {
	//
	// StackPane textStack = new StackPane();
	// TextArea textField = new TextArea(text);
	// textField.setEditable(false);
	// textField.setWrapText(true);
	// textField
	// .setStyle(" -fx-background-color: transparent; -fx-background-insets: 0; -fx-background-radius: 0;");
	// // the invisible label is a hack to get the textField to size like a
	// // label.
	// Label invisibleLabel = new Label();
	// invisibleLabel.textProperty().bind(invisibleLabel.textProperty());
	// // invisibleLabel.setVisible(false);
	// textStack.getChildren().addAll(invisibleLabel, textField);
	// invisibleLabel.textProperty().bindBidirectional(textField.textProperty());
	// invisibleLabel.setGraphic(textStack);
	// invisibleLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	// return invisibleLabel;
	// }

}
