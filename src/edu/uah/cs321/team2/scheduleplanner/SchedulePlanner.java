package edu.uah.cs321.team2.scheduleplanner;

import edu.uah.cs321.team2.scheduleplanner.view.CompositeScheduleViewController;
import edu.uah.cs321.team2.scheduleplanner.view.PeopleListViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import edu.uah.cs321.team2.scheduleplanner.model.CompositeSchedule;

/**
 * Main Application Class for the Schedule Planner application
 * @author Team 2
 */
public class SchedulePlanner extends Application {
    
    /**
     * Overridden start method from Application
     * Called automatically during program execution, it sets up the initial view
     * @param stage
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        
        this.compositeSchedule = DataSerializer.loadCompositeSchedule();
        if (this.compositeSchedule == null) {
            // If no schedule found, create composite schedule instance and default shifts
            this.compositeSchedule = new CompositeSchedule();
            this.compositeSchedule.createDefaultShifts();
        }
                
        // Load root Border Pane
        FXMLLoader rootLoader = new FXMLLoader();
        rootLoader.setLocation(SchedulePlanner.class.getResource("view/RootBorder.fxml"));
        BorderPane root = (BorderPane) rootLoader.load();
        
        // Load Composite Schedule view into center pane
        FXMLLoader compositeLoader = new FXMLLoader();
        compositeLoader.setLocation(SchedulePlanner.class.getResource("view/CompositeSchedule.fxml"));
        GridPane compositeSchedulePane = (GridPane) compositeLoader.load();
        
        CompositeScheduleViewController scheduleController = (CompositeScheduleViewController) compositeLoader.getController();
        scheduleController.setCompositeSchedule(this.compositeSchedule);
        
        root.setCenter(compositeSchedulePane);
                
        // Load People List view into right pane
        FXMLLoader peopleLoader = new FXMLLoader();
        peopleLoader.setLocation(SchedulePlanner.class.getResource("view/PeopleList.fxml"));
        VBox peopleList = (VBox) peopleLoader.load();
        
        PeopleListViewController peopleController = (PeopleListViewController) peopleLoader.getController();
        peopleController.setAllPersons(this.compositeSchedule.getPeople());
        peopleController.setDelegate(this.compositeSchedule);
        
        root.setRight(peopleList);
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        compositeSchedule.addListenerToPersonListeners(scheduleController);
        compositeSchedule.addListenerToShiftListeners(scheduleController);
        compositeSchedule.addListenerToPersonListeners(peopleController);
        
        scheduleController.refreshView();
        peopleController.refreshView();
    }
    
    /**
     * Overridden stop method from Application
     * Called automatically during execution, it saves the current composite schedule
     */
    @Override
    public void stop() {
        DataSerializer.saveCompositeSchedule(this.compositeSchedule);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private CompositeSchedule compositeSchedule;
}
