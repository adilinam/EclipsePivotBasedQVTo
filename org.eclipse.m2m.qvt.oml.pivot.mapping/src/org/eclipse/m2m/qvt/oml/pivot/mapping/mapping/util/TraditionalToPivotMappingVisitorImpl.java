package org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.eclipse.m2m.internal.qvt.oml.expressions.Constructor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.m2m.internal.qvt.oml.ast.env.InternalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.emf.util.Logger;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ModelInstance;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ModuleInstance;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl;
import org.eclipse.m2m.internal.qvt.oml.evaluator.TransformationInstance;
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
import org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter;
import org.eclipse.m2m.internal.qvt.oml.expressions.impl.MappingOperationImpl;
import org.eclipse.m2m.internal.qvt.oml.library.EObjectEStructuralFeaturePair;
import org.eclipse.m2m.internal.qvt.oml.library.QvtResolveUtil;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.AssignExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.BlockExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ListType;
import org.eclipse.ocl.expressions.CollectionItem;
import org.eclipse.ocl.expressions.CollectionKind;
import org.eclipse.ocl.expressions.CollectionLiteralExp;
import org.eclipse.ocl.expressions.CollectionLiteralPart;
import org.eclipse.ocl.expressions.CollectionRange;
import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.expressions.PropertyCallExp;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.expressions.VariableExp;
import org.eclipse.ocl.pivot.Operation;
import org.eclipse.ocl.pivot.PivotFactory;
import org.eclipse.ocl.pivot.Type;
import org.eclipse.ocl.pivot.VariableDeclaration;
import org.eclipse.ocl.pivot.utilities.MetamodelManager;
import org.eclipse.osgi.container.SystemModule;
import org.eclipse.qvto.examples.pivot.imperativeocl.ImperativeOCLFactory;
import org.eclipse.qvto.examples.pivot.qvtoperational.MappingCallExp;
import org.eclipse.qvto.examples.pivot.qvtoperational.QVTOperationalFactory;
import org.eclipse.swt.internal.C;

public class TraditionalToPivotMappingVisitorImpl extends QvtOperationalEvaluationVisitorImpl
implements TraditionalToPivotMappingVisitor {

	private QVToFacade qvto;
	private org.eclipse.qvto.examples.pivot.qvtoperational.OperationalTransformation pivotOperationalTransformation;

	public TraditionalToPivotMappingVisitorImpl(QVToFacade qvto, QvtOperationalEnv env,
			QvtOperationalEvaluationEnv evalEnv) {
		super(env, evalEnv);
		this.qvto = qvto;
		pivotOperationalTransformation = qvto.createOperationalTransformation();
	}

	@Override
	public Object visitHelper(Helper helper) {
		org.eclipse.qvto.examples.pivot.qvtoperational.Helper pivotHelper = qvto.createHelper(helper);
		pivotHelper.setBody((org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody) helper.getBody().accept(this));
		return pivotHelper;
	}


	@Override
	public Object visitIteratorExp(IteratorExp<EClassifier, EParameter> callExp)
	{
		org.eclipse.ocl.pivot.IteratorExp pivotIteratorExp = qvto.createIteratorExp();

		pivotIteratorExp.setName(callExp.getName());
		OCLExpression<EClassifier> source = callExp.getSource();
		if(source!=null)
		{
			Object obj=source.accept(this);
			if(obj instanceof org.eclipse.ocl.pivot.OCLExpression)
			{
				pivotIteratorExp.setOwnedSource((org.eclipse.ocl.pivot.OCLExpression) obj);	
			}
		}
		for(Variable<EClassifier, EParameter> iterator:callExp.getIterator())
		{
			pivotIteratorExp.getOwnedIterators().add((org.eclipse.ocl.pivot.Variable) iterator.accept(this));
		}

		return pivotIteratorExp;
	}
	@Override
	public Object visitVariable(Variable<EClassifier, EParameter> variable) {
		org.eclipse.ocl.pivot.Variable pivotVariable = qvto.createVariable();
		pivotVariable.setName(variable.getName());
		//pivotVariable.setType();
		return pivotVariable;
	}
	@Override
	public Object visitPropertyCallExp(PropertyCallExp<EClassifier, EStructuralFeature> callExp) {
		org.eclipse.ocl.pivot.PropertyCallExp pivotPropertyCallExp = qvto.createPropertyCallExp();
		if(callExp.getSource()!=null)
		{
			pivotPropertyCallExp.setOwnedSource((org.eclipse.ocl.pivot.OCLExpression) callExp.getSource().accept(this));
		}
		return pivotPropertyCallExp;
	}

	@Override
	public Object visitOperationCallExp(OperationCallExp<EClassifier, EOperation> callExp) {
		org.eclipse.ocl.pivot.OperationCallExp value = qvto.createOperationCallExp();
		OCLExpression<EClassifier> source = callExp.getSource();
		if (source != null) {

			Object obj=source.accept(this);
			if(obj instanceof org.eclipse.ocl.pivot.OCLExpression)
			{
				value.setOwnedSource((org.eclipse.ocl.pivot.OCLExpression)obj );
			}
		}

		for (OCLExpression<EClassifier> exp : callExp.getArgument()) {
			exp.accept(this);
		}
		EOperation referredOperation = callExp.getReferredOperation(); //FIXME Now convert this operation to pivot
		Logger.getLogger().log(Logger.INFO,"Referred operation: " + referredOperation, referredOperation);
		if (referredOperation instanceof org.eclipse.ocl.utilities.Visitable) {
			// TODO -
			// Should not visit referenced operation, as these might come from different top container (Module)
			// Should strictly traverse on containment basis, no need to track processed nodes
			//doProcess((Visitable) referredOperation, callExp);
		}
		// FIXME: Should return pivot OperationCallExp
		return value;
	}

	@Override
	public Object visitVariableExp(VariableExp<EClassifier, EParameter> v) {

		org.eclipse.ocl.pivot.VariableExp variableExp= qvto.createVariableExp();

		Logger.getLogger().log(Logger.INFO, "Visiting Variable Exp => "+v, v);

		QvtOperationalEvaluationEnv evalEnv = getOperationalEvaluationEnv();

		Variable<EClassifier, EParameter> vd = v.getReferredVariable();
		String varName = vd.getName();
		variableExp.setName(varName);
		Logger.getLogger().log(Logger.INFO, "Variable name => "+varName, varName);
		Object value = evalEnv.getValueOf(varName);

		Logger.getLogger().log(Logger.INFO, "Variable value => "+value, value);
		EClassifier variableType = v.getType();


		variableExp.setReferredVariable((VariableDeclaration) v.getReferredVariable().accept(this));
		Logger.getLogger().log(Logger.INFO, "Variable type => "+variableType, variableType);

		if(QvtOperationalEnv.THIS.equals(varName)) {
			EClassifier varType = v.getType();
			if(varType instanceof Module) {
				Module referredThisModule = (Module)varType;
				ModuleInstance thisObj = evalEnv.getThisOfType(referredThisModule);
				// only if not null, the variables is part of the QVT type system.
				// otherwise, it may be a custom variable in non-QVT execution context, like ImperativeOCL 
				if(thisObj != null) {
					return thisObj;
				}
			}
		} else if(vd instanceof ModelParameter) {			
			OperationalTransformation transformation = (OperationalTransformation) vd.eContainer();
			TransformationInstance transformationInstance = (TransformationInstance)evalEnv.getThisOfType(transformation);
			assert transformationInstance != null;

			ModelInstance model = transformationInstance.getModel((ModelParameter)vd);
			assert model != null;			
			return model;
		}

		return variableExp;
	}
	@Override
	public Object visitMappingBody(MappingBody mappingBody) {
		org.eclipse.qvto.examples.pivot.qvtoperational.MappingBody pivotMappingBody = qvto.createMappingBody();

		for(org.eclipse.ocl.ecore.OCLExpression exp:mappingBody.getContent())
		{
			Object obj=exp.accept(this);
			if(obj!=null)
			{
				if(obj instanceof org.eclipse.ocl.pivot.OCLExpression)
				{
					pivotMappingBody.getContent().add((org.eclipse.ocl.pivot.OCLExpression) obj);
				}
			}
		}
		return pivotMappingBody;

	}
	@Override
	public Object visitObjectExp(ObjectExp objectExp) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ObjectExp pivotObjectExp = qvto.createObjectExp();
		objectExp.getBody().accept(this);
		return pivotObjectExp;
	}
	@Override
	public Object visitAssignExp(AssignExp assignExp) {
		org.eclipse.qvto.examples.pivot.imperativeocl.AssignExp pivotAssignExp = qvto.createAssignExpOCL();

		if(assignExp.getLeft()!=null)
		{
			pivotAssignExp.setLeft((org.eclipse.ocl.pivot.OCLExpression) assignExp.getLeft().accept(this));
		}
		if(assignExp.getValue()!=null)
		{
			for(org.eclipse.ocl.ecore.OCLExpression exp:assignExp.getValue())
			{
				Object obj=exp.accept(this);
				if(obj instanceof org.eclipse.ocl.pivot.OCLExpression)
				{
					pivotAssignExp.getValue().add((org.eclipse.ocl.pivot.OCLExpression)obj );
				}
			}

		}

		return pivotAssignExp;
	}

	@SuppressWarnings("restriction")
	@Override
	public Object visitMappingOperation(MappingOperation mappingOperation) {

		org.eclipse.qvto.examples.pivot.qvtoperational.MappingOperation pivotOperation = qvto
				.createMappingOperation(mappingOperation);


		Logger.getLogger().log(Logger.INFO, "Mapping body of -> "+mappingOperation.getName(), mappingOperation);
		pivotOperation.setBody((org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody) mappingOperation.getBody().accept(this));
		// ====== Mapping the body for Mapping operation
		for (OCLExpression<EClassifier> exp : mappingOperation.getWhen()) {
			Logger.getLogger().log(Logger.INFO, "Mapping When of => "+mappingOperation.getName()+" => "+exp, exp);
			pivotOperation.setWhen((org.eclipse.ocl.pivot.OCLExpression) exp.accept(this));
		}
		if (mappingOperation.getWhere() instanceof BlockExp) {
			for (OCLExpression<EClassifier> exp : ((BlockExp) mappingOperation.getWhere()).getBody()) {
				Logger.getLogger().log(Logger.INFO, "Mapping Where of => "+mappingOperation.getName()+" => "+exp, exp);
				exp.accept(this);
			}
		}
		// ============================
		return pivotOperation;
	}

	@Override
	public Object visitEntryOperation(EntryOperation entryOperation) {
		org.eclipse.qvto.examples.pivot.qvtoperational.EntryOperation pivotEntryOperation = qvto
				.createEntryOperation(entryOperation);
		pivotOperationalTransformation.setEntry(pivotEntryOperation);
		return pivotEntryOperation;
	}

	@Override
	public Object visitOperationBody(OperationBody operationBody) {
		org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody pivotOperationBody = qvto.createOperationBody();
		for(org.eclipse.ocl.ecore.OCLExpression exp:operationBody.getContent())
		{
			Object obj=exp.accept(this);
			if(obj!=null)
			{
				pivotOperationBody.getContent().add((org.eclipse.ocl.pivot.OCLExpression) obj);
			}
		}
		return pivotOperationBody;
	}
	@Override
	public Object visitCollectionLiteralExp(CollectionLiteralExp<EClassifier> cl) {

		org.eclipse.ocl.pivot.CollectionLiteralExp pivotCollectionLiteralExp = qvto.createCollectionLiteralExp();

		for(CollectionLiteralPart<EClassifier> parts:cl.getPart())
		{
			Object obj= parts.accept(this);
			if(obj!=null)
			{
				pivotCollectionLiteralExp.getOwnedParts().add((org.eclipse.ocl.pivot.CollectionLiteralPart) obj);
			}
		}
		if(cl.getKind().getName().equalsIgnoreCase("Set"))
		{
			pivotCollectionLiteralExp.setKind(org.eclipse.ocl.pivot.CollectionKind.SET);
		}
		return pivotCollectionLiteralExp;

	}
	@Override
	public Object visitModule(Module module) {
		java.util.@NonNull List<Operation> pivotOperations = pivotOperationalTransformation.getOwnedOperations();

		EList<EOperation> list = module.getEOperations();
		for (EOperation eOperation : list) {
			Operation tempOperation = (Operation) ((ImperativeOperation) eOperation).accept(this);
			//((ImperativeOperation)eOperation).getBody().accept(this);
			// Adding by reference into pivot OperationalTransformation
			pivotOperations.add(tempOperation);
		}
		// pivotOperationalTransformation.accept(new
		// QVTOperationalToStringVisitor(new StringBuilder()));
		return pivotOperationalTransformation;
	}
	@Override
	public Object visitConstructor(Constructor constructor)
	{
		org.eclipse.qvto.examples.pivot.qvtoperational.Constructor pivotConstructor= qvto.createConstructor(constructor);
		return pivotConstructor;	
	}
}
