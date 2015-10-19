package org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
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
import org.eclipse.qvto.examples.pivot.qvtoperational.MappingOperation;
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
		pivotHelper.setName(traditionalHelper.getName());
//		pivotHelper.setType(metamodelManager.getASOfEcore(Type.class, traditionalHelper.getEType()));	// FIXME Bug 479445
		((PivotObjectImpl)pivotHelper).setESObject(traditionalHelper);
		return pivotHelper;
	}
	
	public @NonNull MappingOperation createMappingOperation(@NonNull EOperation traditionalMappingOperation) 
	{
		MappingOperation pivotMappingOperation = QVTOperationalFactory.eINSTANCE.createMappingOperation();
		pivotMappingOperation.setName(traditionalMappingOperation.getName());
		pivotMappingOperation.setType(metamodelManager.getASOfEcore(Type.class, traditionalMappingOperation.getEType()));
		((PivotObjectImpl)pivotMappingOperation).setESObject(traditionalMappingOperation);
		return pivotMappingOperation;
	}
	
	public @NonNull EntryOperation createEntryOperation(@NonNull EOperation traditionalEntryOperation) 
	{
		EntryOperation pivotEntryOperation= QVTOperationalFactory.eINSTANCE.createEntryOperation();
		pivotEntryOperation.setName(traditionalEntryOperation.getName());
		pivotEntryOperation.setType(metamodelManager.getASOfEcore(Type.class, traditionalEntryOperation.getEType()));
		((PivotObjectImpl)pivotEntryOperation).setESObject(traditionalEntryOperation);
		
		return pivotEntryOperation;
		
	}
	
	
}
