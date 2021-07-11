package com.bjn.ui;

import java.io.File;
import java.util.Collection;

import com.bjn.controller.MainController;
import com.bjn.data.EmployeeCouple;
import com.bjn.dataretriever.DataRetriever;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class UIApplication extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane pane = new BorderPane ();
		primaryStage.setTitle ("Best Employees in the company");
		FileChooser fileChooser = new FileChooser ();
		fileChooser.setTitle ("Open Resource File");
		fileChooser.getExtensionFilters ().add (new FileChooser.ExtensionFilter ("Text Files", "*.txt"));
		Button button = new Button ("Select File");	
		Label chooseLbl = new Label ("Choose file");
		chooseLbl.setFont (new Font ("Arial", 18));
		HBox buttonBox = new HBox ();
		buttonBox.setPadding (new Insets (10, 10, 10, 10));
		buttonBox.alignmentProperty ().set (Pos.CENTER);
		buttonBox.setSpacing (20);
		buttonBox.getChildren ().addAll (chooseLbl, button);
		pane.setTop (buttonBox);
		VBox tableBox = new VBox ();
		tableBox.alignmentProperty ().set (Pos.CENTER);
		tableBox.setSpacing (20);
		tableBox.setPadding (new Insets (10, 10, 10, 10));
		Label tabelLabel = new Label ("Employees that worked together longest on a project in days.");
		tabelLabel.setFont (new Font ("Arial", 16));
		TableView<EmployeeCouple> table = new TableView<EmployeeCouple> ();
		tableBox.getChildren ().addAll (tabelLabel, table);
		TableColumn<EmployeeCouple, Integer> employee1Id = new TableColumn<EmployeeCouple, Integer> ("Employee 1 ID");
		TableColumn<EmployeeCouple, Integer> employee2Id = new TableColumn<EmployeeCouple, Integer> ("Employee 2 ID");
		TableColumn<EmployeeCouple, Integer> projectId = new TableColumn<EmployeeCouple, Integer> ("Project ID");
		TableColumn<EmployeeCouple, Integer> daysWorkedTogether = new TableColumn<EmployeeCouple, Integer> ("Days worked together");
		employee1Id.setCellValueFactory (new PropertyValueFactory<EmployeeCouple, Integer> ("employee1Id"));
		employee2Id.setCellValueFactory (new PropertyValueFactory<EmployeeCouple, Integer> ("employee2Id"));
		projectId.setCellValueFactory (new PropertyValueFactory<EmployeeCouple, Integer> ("projectId"));
		daysWorkedTogether.setCellValueFactory (new Callback<CellDataFeatures<EmployeeCouple, Integer>, ObservableValue<Integer>> () {
			public ObservableValue<Integer> call(CellDataFeatures<EmployeeCouple, Integer> t) {
				return new ReadOnlyObjectWrapper<Integer> (t.getValue ().calculateTotalEngagemant ());
			}
		});
		employee1Id.prefWidthProperty ().bind (table.widthProperty ().multiply (0.25));
		employee2Id.prefWidthProperty ().bind (table.widthProperty ().multiply (0.25));
		projectId.prefWidthProperty ().bind (table.widthProperty ().multiply (0.25));
		daysWorkedTogether.prefWidthProperty ().bind (table.widthProperty ().multiply (0.25));
		table.getColumns ().addAll (employee1Id, employee2Id, projectId, daysWorkedTogether);

		button.setOnAction (e -> {
			File selectedFile = fileChooser.showOpenDialog (primaryStage);
			if (selectedFile != null) {
				table.setItems (processData (selectedFile));
			}
		});

		pane.setCenter (tableBox);
		Scene scene = new Scene (pane, 600, 400);
		primaryStage.setScene (scene);
		primaryStage.show ();

	}

	private ObservableList<EmployeeCouple> processData(File file) {
		DataRetriever retriever = new DataRetriever ();
		MainController ctrl = new MainController ();
		retriever.createFormats ();
		ctrl.createRawDataMap (retriever.retrieveData (file));
		ObservableList<EmployeeCouple> data = FXCollections.observableArrayList ();
		Collection<EmployeeCouple> result = ctrl.getCouples ();
		for (EmployeeCouple emp : result) {
			data.add (emp);
		}
		return data;

	}

	public static void main(String args[]) {
		launch ();
	}

}
