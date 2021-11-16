package dad.codegen.ui;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import dad.codegen.model.javafx.Bean;
import dad.codegen.model.javafx.FXModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController implements Initializable {
	
	//Model
	
	private ObjectProperty<FXModel> fxModel = new SimpleObjectProperty<>();
	
	private StringProperty packageName = new SimpleStringProperty();
	private ListProperty<Bean> beans = new SimpleListProperty<Bean>(FXCollections.observableArrayList());
	
	//Vista
	
	  @FXML
	    private Button abrirButton;

	    @FXML
	    private Button eliminarBeanButton;

	    @FXML
	    private Button generarButton;

	    @FXML
	    private Button guardarButton;
	    
	    @FXML
	    private ListView<Bean> beanList;

	    @FXML
	    private VBox noBeanPane;

	    @FXML
	    private Button newBeanButton;

	    @FXML
	    private Button nuevoButton;

	    @FXML
	    private TextField paqueteText;

	    @FXML
	    private BorderPane rightPane;

	    @FXML
	    private GridPane view;
	    
	    public GridPane getView() {
	    	return view;
	    }
	    
	    public MainController() throws IOException {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
			loader.setController(this);
			loader.load();
	    }
	    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//Bindings
		paqueteText.textProperty().bindBidirectional(packageName);
		beanList.itemsProperty().bind(beans);
		
		//Listeners
		fxModel.addListener((o,ov,nv) -> onFXModelChanged(o,ov,nv));
		
		
		fxModel.set(new FXModel());
		
	}
	
	private void onFXModelChanged(ObservableValue<? extends FXModel> o, FXModel ov, FXModel nv) {
		
		if(ov != null) {
			System.out.println("desvinculamos el viejo: "+ov.getPackageName());
			packageName.unbindBidirectional(ov.packageNameProperty());
			beans.unbind();
		}
		
		if (nv != null) {
			
			System.out.println("vinculando el nuevo " +nv.getPackageName());
			packageName.bindBidirectional(nv.packageNameProperty());
			beans.bind(nv.beansProperty());
		}
	}
	
	
	
	@FXML
    void onAbrirAction(ActionEvent event) {
		
		if(App.confirm("Nuevo modelo FX", "Se va a crear un nuevo modelo FX.\n\nLos cambios que haya realizado en el modelo actual se perderán.","¿Desea continuar?"));
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Abrir modelo FX");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Modelo FX(*.fx)", "*.fx"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Todos los ficheros (*.*)", "*.*"));
		fileChooser.setInitialDirectory(new File("."));
		File fichero = fileChooser.showOpenDialog(App.getPrimaryStage());
		
		if (fichero != null) {
			
			try {
				fxModel.set(FXModel.load(fichero));
			} catch (Exception e) {
				App.error("Error al abrir el modelo FX desde el fichero" +fichero.getName()+".",e.getMessage());
			}
			
		}

    }

    @FXML
    void onAnadirAction(ActionEvent event) {

    }

    @FXML
    void onEliminarAction(ActionEvent event) {

    }

    @FXML
    void onGenerarAction(ActionEvent event) {
    	
    	DirectoryChooser dirChooser = new DirectoryChooser();
    	dirChooser.setTitle("Seleccionar carpeta donde general código");
    	dirChooser.setInitialDirectory(new File("."));
    	File directorio = dirChooser.showDialog(App.getPrimaryStage());
    	
    	if (directorio != null) {
			
    		try {
				FXModel model = fxModel.get();
				model.generateCode(directorio);
				App.info("Generar código del modelo FX", "Se ha generado el código correctamente en el directorio "+directorio.getName()+".");    			
			} catch (IOException e) {
				e.printStackTrace();
				App.error("Error al generar el modelo FX en el directorio "+directorio.getName()+".", e.getCause().getMessage());
			}
		}
    	

    }

    @FXML
    void onGuardarButton(ActionEvent event) {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar modelo FX");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Modelo FX(*.fx)", "*.fx"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Todos los ficheros (*.*)", "*.*"));
		fileChooser.setInitialDirectory(new File("."));
		File fichero = fileChooser.showSaveDialog(App.getPrimaryStage());
		
		if (fichero != null) {
			
			try {
				FXModel modelo = fxModel.get();
				modelo.save(fichero);
	
			} catch (Exception e) {
				App.error("Error al abrir el guardar FX desde el fichero" +fichero.getName()+".",e.getCause().getMessage());
			}
			
		}

    }

    @FXML
    void onNuevoAction(ActionEvent event) {
    	
    	if(App.confirm("Nuevo modelo FX", "Se va a crear un nuevo modelo FX.\n\nLos cambios que haya realizado en el modelo actual se perderán.","¿Desea continuar?")) {
    		fxModel.set(new FXModel());
    	}

    }

}
