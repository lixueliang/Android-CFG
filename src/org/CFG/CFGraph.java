package org.CFG;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import soot.Body;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

//import soot.jimple.infoflow.android.SetupApplication;

import soot.Unit;
import soot.ValueBox;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.spark.SparkTransformer;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;


public class CFGraph {


	public CFGraph() {

	// TODO Auto-generated constructor stub

	}


	public static void main(String[] args) {

	   // TODO Auto-generated method stub
	   SetupApplication app = new SetupApplication("/Users/xueliang/Documents/tools/sootpackages/android-4.0.3_r1.jar","/Users/xueliang/Documents/tools/AndroidInstrument/cocos2d_android.apk");

	   try {
	      app.calculateSourcesSinksEntrypoints("/Users/xueliang/Documents/workspace/soot-infoflow-android/SourcesAndSinks.txt");
	   } catch (IOException e) {
	   // TODO Auto-generated catch block
	      e.printStackTrace();
	   } catch (XmlPullParserException e) {
	   // TODO Auto-generated catch block
	      e.printStackTrace();
       }
	   
	   // Initial settings
       soot.G.reset();
	   Options.v().set_src_prec(Options.src_prec_apk);
	   Options.v().set_process_dir(Collections.singletonList("/Users/xueliang/Documents/tools/AndroidInstrument/cocos2d_android.apk"));
	   Options.v().set_android_jars("/Users/xueliang/Documents/tools/sootpackages");
	   Options.v().set_whole_program(true);
	   Options.v().set_allow_phantom_refs(true);
	   Options.v().set_output_format(Options.output_format_none);
	   Options.v().setPhaseOption("cg.spark", "on");
	   
       // Load all the classes
       Scene.v().loadNecessaryClasses(); 
       
       // Creat a dummy main method
       SootMethod entryPoint = app.getEntryPointCreator().createDummyMain();
       Options.v().set_main_class(entryPoint.getSignature());
       Scene.v().setEntryPoints(Collections.singletonList(entryPoint));
	   /////System.out.println(entryPoint.getActiveBody());
       PackManager.v().runPacks();
	   /////System.out.println(Scene.v().getCallGraph());
       
       //Traverse all the classes
       Iterator<SootClass> cit = Scene.v().getApplicationClasses().iterator();
       while (cit.hasNext()){
    	   // Set up the class we are working with
    	   SootClass c = (SootClass) cit.next();
    	   printCFG_basedOn_string(c);
       }
    }
	
	
	public static void printCFG_basedOn_id(SootClass c){
	   String className = c.getName();
	   
	   //Traverse all the methods
 	   Iterator<SootMethod> mit =  c.methodIterator();    	   
 	   while (mit.hasNext()){
 		   //Retrieve method and its body
 		   SootMethod m = mit.next();
 		   String methodName = m.getName();
 		   /////System.out.println(className+"."+methodName);
 		   try{
 		   Body b = m.retrieveActiveBody();
 		   
 		   // Build the CFG
 		   ExceptionalUnitGraph g = new ExceptionalUnitGraph(b);
 		   /////System.out.println("methodCFG: " + className+"."+methodName+"\n\n"+ g.toString());
 		   /////Unit unit = (Unit) g.iterator();
 		   /////System.out.println("methodCFG: "+ g.toString());
 		   
 		   // Print out the instructions needed for Graphviz to draw graphs
 		   System.out.println("\nmethodCFG: " + className+"."+methodName+"\n");
 		   System.out.println("digraph mygraph{");
 		   
 		   // Traverse all the units
 		   Iterator<Unit> uit = g.iterator();
 	       while(uit.hasNext()){
 		        Unit u = uit.next();
 		        int uid = u.hashCode();
 		        
 		        // Obtain the unexceptional successes 
 		        List<Unit> unexcp_seccs = g.getUnexceptionalSuccsOf(u);
 		        for(Unit secc : unexcp_seccs){
 		        	int seccid = secc.hashCode();
 		        	
 		        	// Print out the directed edge 
 		        	System.out.println("   "+uid+" -> "+seccid+";");
 		        }
 		        
 		        // Obtain the exceptional successes
 		        List<Unit> excp_seccs = g.getExceptionalSuccsOf(u);
 		        for(Unit secc : excp_seccs){
		        	int seccid = secc.hashCode();
		        	
		        	// Print out the directed edge
		        	System.out.println("   "+uid+" -> "+seccid+" [style=dotted];");
		        }
 		        
 		   }
 	       System.out.println("}");
 		   
 		   } catch(RuntimeException e)
 		   {e.printStackTrace();}
 	   }
	}


}

