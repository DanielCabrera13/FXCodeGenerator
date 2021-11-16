package dad.codegen;

import java.io.File;

import dad.codegen.model.javafx.Bean;
import dad.codegen.model.javafx.FXModel;
import dad.codegen.model.javafx.Property;
import dad.codegen.model.javafx.Type;

public class Ejemplo1 {

	public static void main(String[] args) throws Exception {
		
		Property real = new Property();
		real.setName("real");
		real.setType(Type.DOUBLE); 
		
		Property imaginario = new Property();
		imaginario.setName("imaginario");
		imaginario.setType(Type.DOUBLE);
		
		Bean complejo = new Bean();
		complejo.setName("Complejo");
		complejo.getProperties().addAll(real,imaginario);
		
//		FXModel modelo = new FXModel();
//		modelo.setPackageName("dad.complejos");
//		modelo.getBeans().addAll(complejo);
		
		FXModel modelo = FXModel.load(new File("complejo.fx"));
		
		System.out.println(modelo.generateBeanCode(modelo.getBeans().get(0)));
		
		modelo.generateCode(new File("ejemplo"));
		

	}

}
