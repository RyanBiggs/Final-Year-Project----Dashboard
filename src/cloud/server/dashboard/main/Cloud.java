/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.server.dashboard.main;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Ryan
 */
    public class Cloud {
        
        public final SimpleStringProperty name;
        public final SimpleStringProperty ID;
     //   private final SimpleObjectProperty<Date> date;
        public final SimpleBooleanProperty alert;
        public final SimpleDoubleProperty runWatt;
        public final SimpleDoubleProperty runCpu;
        public final SimpleDoubleProperty wattGoal;
        public final SimpleDoubleProperty cpuGoal;
        
        public Cloud(String name, String ID, //Date date
                Boolean alert, Double cpuGoal, 
                Double wattGoal, Double runWatt, Double runCpu){
            this.name = new SimpleStringProperty(name);
            this.ID = new SimpleStringProperty(ID);
      //      this.date = new SimpleObjectProperty<>(date);
            this.alert = new SimpleBooleanProperty(alert);
            this.cpuGoal = new SimpleDoubleProperty(cpuGoal);
            this.wattGoal = new SimpleDoubleProperty(wattGoal);
            this.runWatt = new SimpleDoubleProperty(runWatt);
            this.runCpu = new SimpleDoubleProperty(runCpu);
            
        }

        public Double getCpuGoal() {
            return cpuGoal.get();
        }
        
         public Double getWattGoal() {
            return wattGoal.get();
        }
         
          public Double getRunWatt() {
            return runWatt.get();
        }
          
           public Double getRunCpu() {
            return runCpu.get();
        }
           
        public String getName() {
            return name.get();
        }

        public String getID() {
            return ID.get();
        }

//        public Date getDate() {
//            return date.get();
//        }

        public Boolean getAlert() {
            
            return alert.get();
        }
        

            
        }
        
    