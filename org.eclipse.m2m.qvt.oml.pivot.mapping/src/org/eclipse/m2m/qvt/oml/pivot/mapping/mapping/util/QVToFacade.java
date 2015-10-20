package org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody;
import org.eclipse.ocl.pivot.Parameter;
import org.eclipse.ocl.pivot.Type;
import org.eclipse.ocl.pivot.internal.manager.MetamodelManagerInternal;
import org.eclipse.ocl.pivot.internal.resource.ASResourceFactoryRegistry;
import org.eclipse.ocl.pivot.internal.utilities.EnvironmentFactoryInternal;
import org.eclipse.ocl.pivot.internal.utilities.OCLInternal;
import org.eclipse.ocl.pivot.internal.utilities.PivotObjectImpl;
import org.eclipse.ocl.pivot.resource.BasicProjectManager;
import org.eclipse.ocl.pivot.resource.ProjectManager;
import org.eclipse.qvto.examples.pivot.qvtoperational.EntryOperation;
import org.eclipse.qvto.examples.pivot.qvtoperational.Helper;
import org.eclipse.qvto.examples.pivot.qvtoperational.ImperativeOperation;
import org.eclipse.qvto.examples.pivot.qvtoperational.MappingOperation;
import org.eclipse.qvto.examples.pivot.qvtoperational.MappingParameter;
import org.eclipse.qvto.examples.pivot.qvtoperational.QVTOperationalFactory;

public class QVToFacade extends OCLInternal
{
	public static @NonNull QVToFacade newInstance() {
		return newInstance(BasicProjectManager.createDefaultProjectManager(), null);
	}
	
	public static @NonNull QVToFacade newInstance(@NonNull ProjectManager projectManager, @Nullable ResourceSet externalResourceSet) {	
		EnvironmentFactoryInternal environmentFactory = ASResourceFactoryRegistry.INSTANCE.createEnvironmentFactory(projectManager, externalResourceSet);
		QVToFacade qvto = newInstance(environmentFactory);
		if (externalResourceSet != null) {
			environmentFactory.adapt(externalResourceSet);
		}
		return qvto;
	}
	
	public static @NonNull QVToFacade newInstance(@NonNull EnvironmentFactoryInternal environmentFactory) {	
		return new QVToFacade(environmentFactory);
	}
	
	private MetamodelManagerInternal metamodelManager = getMetamodelManager();

	public QVToFacade(@NonNull EnvironmentFactoryInternal environmentFactory) {
		super(environmentFactory);
		metamodelManager = getMetamodelManager();
	}

	public @NonNull Helper createHelper(@NonNull EOperation traditionalHelper) {

		Helper pivotHelper = QVTOperationalFactory.eINSTANCE.createHelper();
		//FIXME Bug 479445
		
		//mapParamters(traditionalHelper,pivotHelper);
		
		pivotHelper.setName(traditionalHelper.getName());
//		pivotHelper.setType(metamodelManager.getASOfEcore(Type.class, traditionalHelper.getEType()));	// FIXME Bug 479445
		((PivotObjectImpl)pivotHelper).setESObject(traditionalHelper);
		return pivotHelper;
	}
	
	private void mapParamters(@NonNull EOperation traditionalHelper, ImperativeOperation pivotImperativeOperation) {

		EList<EParameter> parameters = traditionalHelper.getEParameters();
	//	System.out.println(parameters.get(0).getName());
		List<Parameter> pivotParameters=pivotImperativeOperation.getOwnedParameters();
		for(EParameter parameter: traditionalHelper.getEParameters())
		{
			Parameter pivotParameter=copyParameterData(parameter);
			pivotParameters.add(pivotParameter);
		}
	}

	private Parameter copyParameterData(EParameter parameter) {
		MappingParameter pivotParameter = QVTOperationalFactory.eINSTANCE.createMappingParameter();
		pivotParameter.setName(parameter.getName());
	//	System.out.println(parameter.getName());
		
		pivotParameter.setType(metamodelManager.getASOfEcore(Type.class, parameter.getEType()));
		return pivotParameter;
	}

	public @NonNull MappingOperation createMappingOperation(@NonNull EOperation traditionalMappingOperation) 
	{
		MappingOperation pivotMappingOperation = QVTOperationalFactory.eINSTANCE.createMappingOperation();
		mapParamters(traditionalMappingOperation, pivotMappingOperation);
		mapBody(((org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation)traditionalMappingOperation).getBody(),pivotMappingOperation);
		
		pivotMappingOperation.setName(traditionalMappingOperation.getName());
		pivotMappingOperation.setType(metamodelManager.getASOfEcore(Type.class, traditionalMappingOperation.getEType()));
		((PivotObjectImpl)pivotMappingOperation).setESObject(traditionalMappingOperation);
		return pivotMappingOperation;
	}
	
	private void mapBody(OperationBody body, MappingOperation pivotMappingOperation) {
		org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody operationBody = QVTOperationalFactory.eINSTANCE.createOperationBody();
		//body.accept();
	}

	public @NonNull EntryOperation createEntryOperation(@NonNull EOperation traditionalEntryOperation) 
	{
		EntryOperation pivotEntryOperation= QVTOperationalFactory.eINSTANCE.createEntryOperation();
		mapParamters(traditionalEntryOperation, pivotEntryOperation);
		pivotEntryOperation.setName(traditionalEntryOperation.getName());
		pivotEntryOperation.setType(metamodelManager.getASOfEcore(Type.class, traditionalEntryOperation.getEType()));
		((PivotObjectImpl)pivotEntryOperation).setESObject(traditionalEntryOperation);
		
		return pivotEntryOperation;
		
	}
//
//	public Object createVisitOperationBody(OperationBody operationBody) {
//		// TODO Auto-generated method stub
//		org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody pivotOperationBody= QVTOperationalFactory.eINSTANCE.createOperationBody();
//		//setting it as a reference
//		EList<org.eclipse.ocl.pivot.OCLExpression> expressions= pivotOperationBody.getContent();
//		for (OCLExpression<EClassifier> exp : operationBody.getContent()) {
//			exp.accept(this);
//		 }
//
//		return null;
//	}
	
	
}
