package temp;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.qvt.oml.BasicModelExtent;
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ModelExtent;
import org.eclipse.m2m.qvt.oml.TransformationExecutor;
import org.eclipse.m2m.qvt.oml.mapping.pivot.test.QvtOperationalMappingArgumentsContainer;
import org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util.QVToFacade;
import org.eclipse.ocl.pivot.utilities.XMIUtil;
import org.junit.Test;

import junit.framework.TestCase;
import rdb.RdbPackage;
import simpleuml.SimpleumlPackage;

public class SimpleUML2RDBtest extends TestCase {

	private static final String qvtoFileUri = System.getProperty("user.dir") + "/Example/Simpleuml_To_Rdb.qvto";
	private static final String inUri = System.getProperty("user.dir") + "/Example/pim.simpleuml";
	private static final String outUri = System.getProperty("user.dir") + "/Example/pim.rdb";

	@Test
	public void testMapping() throws IOException {
		executeTransformation();
	}

	/**
	 * 
	 * programmatic execution for collecting arguments
	 */
	protected void executeTransformation() throws IOException {
		EPackage.Registry.INSTANCE.put(SimpleumlPackage.eNS_URI, SimpleumlPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(RdbPackage.eNS_URI, RdbPackage.eINSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

		// Refer to an existing transformation via URI
		URI transformationURI = URI.createFileURI(qvtoFileUri);
		// create executor for the given transformation
		TransformationExecutor executor = new TransformationExecutor(transformationURI);

		// define the transformation input
		// Remark: we take the objects from a resource, however
		// a list of arbitrary in-memory EObjects may be passed
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
				new EcoreResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new EcoreResourceFactoryImpl());

		Resource inResource = resourceSet.getResource(URI.createFileURI(inUri), true);
		// Resource inResource = getResourceFromUri(inUri);
		EList<EObject> inObjects = inResource.getContents();

		// create the input extent with its initial contents
		ModelExtent input = new BasicModelExtent(inObjects);
		// create an empty extent to catch the output
		ModelExtent output = new BasicModelExtent();

		// setup the execution environment details ->
		// configuration properties, logger, monitor object etc.
		ExecutionContextImpl context = new ExecutionContextImpl();
		context.setConfigProperty("keepModeling", true);

		// run the transformation assigned to the executor with the given
		// input and output and execution context -> ChangeTheWorld(in, out)
		// Remark: variable arguments count is supported
		ExecutionDiagnostic result = executor.execute(context, input, output);
		System.out.println("Transformation executed.");
		
		
		// check the result for success
		if(result.getSeverity() == Diagnostic.OK) {
			// the output objects got captured in the output extent
			List<EObject> outObjects = output.getContents();
			// let's persist them using a resource 
		        ResourceSet resourceSet2 = new ResourceSetImpl();
			Resource outResource = resourceSet2.getResource(URI.createFileURI(outUri), true);
			outResource.getContents().addAll(outObjects);
			outResource.save(Collections.emptyMap());
		}  
				
		/*org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation operationalTransformation = QvtOperationalMappingArgumentsContainer
				.getInstance().getOperationalTransformation();
		
		try {
			writeTraditionalQVTOperationToXML(QVToFacade.newInstance(), operationalTransformation, "traditionalAS_UML2RDB");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
	}
	
	public static void writeTraditionalQVTOperationToXML(QVToFacade qvto, OperationalTransformation transformation, String filename) throws IOException{
		ResourceSet resourceSet = qvto.getResourceSet();
		Resource resource = resourceSet.createResource(URI.createFileURI("/Example/"+filename+".qvtoas"));
		// add the root object to the resource 
		resource.getContents().add(transformation); 
		// serialize resource you can specify also serialization 
		resource.save(createSaveOptions());
	}
	
	private static Map<Object, Object> createSaveOptions() {
		Map<Object, Object> saveOptions = XMIUtil.createSaveOptions();
		saveOptions.put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
		saveOptions.put(XMLResource.OPTION_URI_HANDLER, new URIHandlerImpl.PlatformSchemeAware());
		saveOptions.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
		saveOptions.put(XMLResource.OPTION_SCHEMA_LOCATION_IMPLEMENTATION, Boolean.TRUE);
		return saveOptions;
	}

}
