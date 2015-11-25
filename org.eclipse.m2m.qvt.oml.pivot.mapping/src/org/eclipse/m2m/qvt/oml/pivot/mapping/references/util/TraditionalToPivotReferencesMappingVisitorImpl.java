package org.eclipse.m2m.qvt.oml.pivot.mapping.references.util;

import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.emf.util.Logger;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ModelInstance;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ModuleInstance;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl;
import org.eclipse.m2m.internal.qvt.oml.evaluator.TransformationInstance;
import org.eclipse.m2m.internal.qvt.oml.expressions.Constructor;
import org.eclipse.m2m.internal.qvt.oml.expressions.ConstructorBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.EntryOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Helper;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelParameter;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.ObjectExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.AssignExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.BlockExp;
import org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util.MappingQueue;
import org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util.QVToFacade;
import org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util.TraditionalToPivotMappingVisitor;
import org.eclipse.ocl.expressions.CollectionLiteralExp;
import org.eclipse.ocl.expressions.CollectionLiteralPart;
import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.expressions.PropertyCallExp;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.expressions.VariableExp;
import org.eclipse.ocl.pivot.Operation;
import org.eclipse.ocl.utilities.Visitable;

public class TraditionalToPivotReferencesMappingVisitorImpl extends QvtOperationalEvaluationVisitorImpl
implements TraditionalToPivotReferencesMappingVisitor{

	private QVToFacade qvto;

	protected Object doProcess(Visitable e) {
		return e.accept(this);
	}
	
	public TraditionalToPivotReferencesMappingVisitorImpl(QVToFacade qvto, QvtOperationalEnv env,
			QvtOperationalEvaluationEnv evalEnv) {
		super(env, evalEnv);
		this.qvto=qvto;
			}

	@Override
	public Object visitHelper(Helper helper) {
		// TODO Auto-generated method stub
		
		doProcess(helper.getBody());
		return null;
	}
	@Override
	public Object visitVariable(Variable<EClassifier, EParameter> variable) {
		//doProcess(variable);
		return null;
	}


	@Override
	public Object visitMappingOperation(MappingOperation mappingOperation) {
		// TODO Auto-generated method stub
		for (OCLExpression<EClassifier> exp : mappingOperation.getWhen()) {
			 doProcess(exp);
		}
		if (mappingOperation.getWhere() instanceof BlockExp) {
			for (OCLExpression<EClassifier> exp : ((BlockExp) mappingOperation.getWhere()).getBody()) {
				doProcess(exp);
			}
		}
		return null;
	}
	public Object visitVariableExp(VariableExp<EClassifier, EParameter> v) {
		Operation referredOperation = qvto.createOperation();
		Variable<EClassifier, EParameter> referredVariable = v.getReferredVariable();
		org.eclipse.ocl.pivot.VariableExp variableExp=(org.eclipse.ocl.pivot.VariableExp) MappingQueue.getInstance().getPivotObject(v);
		referredOperation.setName(variableExp.getName());
		
		//doProcess(v);
		
		return null;
	}

	
	@Override
	public Object visitOperationCallExp(OperationCallExp<EClassifier, EOperation> callExp) {
		OCLExpression<EClassifier> source = callExp.getSource();
		if (source != null) {
			Object obj = doProcess(source);
		}

		for (OCLExpression<EClassifier> exp : callExp.getArgument()) {
			Object obj = doProcess(exp);
			}
		return null;
	}
	
	@Override
	public Object visitEntryOperation(EntryOperation entryOperation) {
		// TODO Auto-generated method stub
		doProcess(entryOperation.getBody());
		return null;
	}
		@Override
	public Object visitModule(Module module) {
		EList<EOperation> list = module.getEOperations();
		for (EOperation eOperation : list) {
			doProcess((ImperativeOperation) eOperation);
		}

		return null;
	}

	@Override
	public Object visitAssignExp(AssignExp assignExp) {
		// TODO Auto-generated method stub
		if (assignExp.getLeft() != null) {
			 doProcess(assignExp.getLeft());
		}
		if (assignExp.getValue() != null) {
			for (org.eclipse.ocl.ecore.OCLExpression exp : assignExp.getValue()) {
				Object obj = doProcess(exp);
			}

		}

		return null;
	}

	@Override
	public Object visitObjectExp(ObjectExp objectExp) {
		// TODO Auto-generated method stub
		org.eclipse.qvto.examples.pivot.qvtoperational.ObjectExp pivotObject = (org.eclipse.qvto.examples.pivot.qvtoperational.ObjectExp) MappingQueue.getInstance().getPivotObject(objectExp);
		org.eclipse.ocl.ecore.Variable referredObject = objectExp.getReferredObject();
		pivotObject.setReferredObject(qvto.createVariable(referredObject));
		return null;
	}

	@Override
	public Object visitMappingBody(MappingBody mappingBody) {
		// TODO Auto-generated method stub
		
		for (org.eclipse.ocl.ecore.OCLExpression exp : mappingBody.getContent()) {
			Object obj = doProcess(exp);
		}
		for (OCLExpression<EClassifier> initExp : mappingBody.getInitSection()) {
			doProcess(initExp);
		}
		return null;
	}

	@Override
	public Object visitPropertyCallExp(PropertyCallExp<EClassifier, EStructuralFeature> callExp) {
		// TODO Auto-generated method stub
		if (callExp.getSource() != null) {
			 doProcess(callExp.getSource());
		}
		EStructuralFeature referredproperty = callExp.getReferredProperty();

		return null;
	}

	@Override
	public Object visitIteratorExp(IteratorExp<EClassifier, EParameter> callExp) {
		// TODO Auto-generated method stub
		
		OCLExpression<EClassifier> source = callExp.getSource();
		if (source != null) {
			Object obj = doProcess(source);
			if (obj instanceof org.eclipse.ocl.pivot.OCLExpression) {
				org.eclipse.ocl.pivot.OCLExpression oclExpression = (org.eclipse.ocl.pivot.OCLExpression) obj;
			}
		}
		for (Variable<EClassifier, EParameter> iterator : callExp.getIterator()) {
			doProcess(iterator);
		}
		
		return null;
	}
	public Object visitConstructorBody(ConstructorBody constructorBody) {
		for (org.eclipse.ocl.ecore.OCLExpression exp : constructorBody.getContent()) {
			doProcess(exp);
		}
		return null;
	}
		
	public Object visitCollectionLiteralExp(CollectionLiteralExp<EClassifier> cl) {
		for (CollectionLiteralPart<EClassifier> parts : cl.getPart()) {
			Object obj = doProcess(parts);
		}
		return null;
	}

	@Override
	public Object visitConstructor(Constructor constructor) {
		doProcess(constructor.getBody());
		return null;
	}

	@Override
	public Object visitOperationBody(OperationBody operationBody) {
		
		for (org.eclipse.ocl.ecore.OCLExpression exp : operationBody.getContent()) {
			doProcess(exp);
		}
		return null;
	}



}
