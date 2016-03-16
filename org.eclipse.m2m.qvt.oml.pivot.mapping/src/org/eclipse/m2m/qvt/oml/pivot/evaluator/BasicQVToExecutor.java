/*******************************************************************************
 * Copyright (c) 2015 Willink Transformations and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     E.D.Willink - initial API and implementation
 ******************************************************************************/
package org.eclipse.m2m.qvt.oml.pivot.evaluator;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ocl.pivot.Model;
import org.eclipse.ocl.pivot.Variable;
import org.eclipse.ocl.pivot.evaluation.EvaluationVisitor;
import org.eclipse.ocl.pivot.internal.evaluation.AbstractExecutor;
import org.eclipse.ocl.pivot.resource.ASResource;
import org.eclipse.ocl.pivot.resource.CSResource;
import org.eclipse.ocl.pivot.utilities.ClassUtil;
import org.eclipse.ocl.pivot.utilities.EnvironmentFactory;
import org.eclipse.ocl.pivot.utilities.PivotUtil;
import org.eclipse.ocl.xtext.completeocl.CompleteOCLStandaloneSetup;
import org.eclipse.qvt.declarative.ecore.QVTBase.Transformation;
import org.eclipse.qvtd.pivot.qvtcorebase.PropertyAssignment;
import org.eclipse.qvtd.pivot.qvtcorebase.RealizedVariable;
import org.eclipse.qvtd.pivot.qvtimperative.Mapping;
import org.eclipse.qvtd.pivot.qvtimperative.MappingCall;
import org.eclipse.qvtd.pivot.qvtimperative.evaluation.QVTiEnvironmentFactory;
import org.eclipse.qvtd.pivot.qvtimperative.evaluation.QVTiEvaluationEnvironment;
import org.eclipse.qvtd.pivot.qvtimperative.evaluation.QVTiExecutor;
import org.eclipse.qvtd.pivot.qvtimperative.evaluation.QVTiModelManager;
import org.eclipse.qvtd.pivot.qvtimperative.evaluation.QVTiTransformationAnalysis;
import org.eclipse.qvto.examples.pivot.qvtoperational.OperationalTransformation;


public class BasicQVToExecutor extends AbstractExecutor implements QVTiExecutor
{
    protected final @NonNull OperationalTransformation transformation;

    private static @NonNull QVTiTransformationAnalysis createTransformationAnalysis(@NonNull  QVTiEnvironmentFactory environmentFactory, @NonNull OperationalTransformation transformation) {
		QVTiTransformationAnalysis transformationAnalysis = environmentFactory.createTransformationAnalysis();
    //	transformationAnalysis.analyzeTransformation(transformation);
		return transformationAnalysis;
	}
    public QVToPivotEvaluationVisitor createQVToPivotEvaluationVisitor()
    {
    	return new QVToPivotEvaluationVisitor(this);
    }
    
    public static @NonNull OperationalTransformation loadTransformation(@NonNull Class<? extends Model> modelClass, @NonNull EnvironmentFactory environmentFactory, @NonNull URI transformationURI, boolean keepDebug) throws IOException {
		// Load the transformation resource
        CSResource xtextResource = (CSResource) environmentFactory.getResourceSet().getResource(transformationURI, true);
        if (xtextResource == null) {
            throw new IOException("Failed to load '" + transformationURI + "'");
        }
		String csMessage = PivotUtil.formatResourceDiagnostics(ClassUtil.nonNullEMF(xtextResource.getErrors()), "Failed to load '" + transformationURI + "'", "\n");
		if (csMessage != null) {
			throw new IOException(csMessage);
		}
		try {
			ASResource asResource = xtextResource.getASResource();
			String asMessage = PivotUtil.formatResourceDiagnostics(ClassUtil.nonNullEMF(asResource.getErrors()), "Failed to load '" + asResource.getURI() + "'", "\n");
			if (asMessage != null) {
				throw new IOException(asMessage);
			}
			for (EObject eContent : asResource.getContents()) {
				if (modelClass.isInstance(eContent)) {
	    			for (org.eclipse.ocl.pivot.Package asPackage : ((Model)eContent).getOwnedPackages()) {
    	    			for (org.eclipse.ocl.pivot.Class asClass : asPackage.getOwnedClasses()) {
    	    				if (asClass instanceof OperationalTransformation) {
    	    	                return (OperationalTransformation)asClass;
    	    				}
    	    			}
	    			}
				}
			}
		} finally {
			if (!keepDebug && (xtextResource instanceof CSResource.CSResourceExtension)) {
				((CSResource.CSResourceExtension)xtextResource).dispose();
			}
		}
        throw new IOException("Failed to locate a transformation in '" + transformationURI + "'");
	}
//	public BasicQVToExecutor(@NonNull QVTiEnvironmentFactory environmentFactory, @NonNull URI transformationURI) throws IOException {
//    	this(environmentFactory, loadTransformation(ImperativeModel.class, environmentFactory, transformationURI, environmentFactory.keepDebug()));
//    }

    public BasicQVToExecutor(@NonNull QVTiEnvironmentFactory environmentFactory, @NonNull OperationalTransformation transformation) {
    	
    	this(environmentFactory, transformation, createTransformationAnalysis(environmentFactory, transformation));
    	CompleteOCLStandaloneSetup.doSetup();
    }
    

    private BasicQVToExecutor(@NonNull QVTiEnvironmentFactory environmentFactory, @NonNull OperationalTransformation transformation, @NonNull QVTiTransformationAnalysis transformationAnalysis) {
		super(environmentFactory, new QVTiModelManager(transformationAnalysis));
		this.transformation = transformation;
	}

	@Override
	public @NonNull QVTiEvaluationEnvironment getEvaluationEnvironment() {
		// TODO Auto-generated method stub
		return (QVTiEvaluationEnvironment) super.getEvaluationEnvironment();
	}

	@Override
	public @NonNull QVTiModelManager getModelManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object internalExecuteMapping(@NonNull Mapping mapping,
			@NonNull EvaluationVisitor undecoratedVisitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object internalExecuteMappingCall(@NonNull MappingCall mappingCall,
			@NonNull Map<Variable, Object> variable2value, @NonNull EvaluationVisitor undecoratedVisitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void internalExecutePropertyAssignment(@NonNull PropertyAssignment propertyAssignment,
			@NonNull Object slotObject, @Nullable Object ecoreValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public @Nullable Object internalExecuteRealizedVariable(@NonNull RealizedVariable realizedVariable,
			@NonNull EvaluationVisitor undecoratedVisitor) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Boolean execute() {
		initializeEvaluationEnvironment(transformation);
		getRootEvaluationEnvironment();
//		Variable ownedContext = QVTbaseUtil.getContextVariable(standardLibrary, transformation);
//		
//		QVTiModelManager modelManager = getModelManager();
//		add(ownedContext, modelManager.getTransformationInstance(transformation));
		
		
       
		// TODO Auto-generated method stub
		transformation.accept(createQVToPivotEvaluationVisitor());
		return null;
		
	}
	public void createModel(@NonNull String outName, @NonNull URI outURI, @Nullable String contentType) {
		// TODO Auto-generated method stub
		
	}
	
	public @Nullable Object internalExecuteTransformation(
			org.eclipse.qvtd.pivot.qvtbase.@NonNull Transformation transformation,
			@NonNull EvaluationVisitor undecoratedVisitor) {
		// TODO Auto-generated method stub
		return null;
	}
	public void loadModel(@NonNull String inName, @NonNull URI inURI, @Nullable String contentType) {
		// TODO Auto-generated method stub
		
	}
	
	public void saveModels() {
		// TODO Auto-generated method stub
		
	}
	
	


}
