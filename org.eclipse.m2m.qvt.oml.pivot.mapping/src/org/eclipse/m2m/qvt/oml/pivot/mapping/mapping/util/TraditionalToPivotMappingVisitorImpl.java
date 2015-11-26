package org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.emf.util.Logger;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ModuleInstance;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl;
import org.eclipse.m2m.internal.qvt.oml.expressions.Constructor;
import org.eclipse.m2m.internal.qvt.oml.expressions.ConstructorBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.EntryOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Helper;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.ObjectExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.AssignExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.BlockExp;
import org.eclipse.ocl.expressions.CollectionLiteralExp;
import org.eclipse.ocl.expressions.CollectionLiteralPart;
import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.expressions.PropertyCallExp;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.expressions.VariableExp;
import org.eclipse.ocl.pivot.Operation;
import org.eclipse.ocl.pivot.utilities.EnvironmentFactory;
import org.eclipse.ocl.utilities.Visitable;
import org.eclipse.qvto.examples.pivot.imperativeocl.utilities.ImperativeOCLToStringVisitor;

public class TraditionalToPivotMappingVisitorImpl extends QvtOperationalEvaluationVisitorImpl
implements TraditionalToPivotMappingVisitor {


	protected Object doProcess(Visitable e) {
		return e.accept(this);
	}

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
		pivotHelper.setBody((org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody) doProcess(helper.getBody()));
		MappingQueue.getInstance().addIncompleteWork(helper, pivotHelper);
		return pivotHelper;
	}

	@Override
	public Object visitIteratorExp(IteratorExp<EClassifier, EParameter> callExp) {
		org.eclipse.ocl.pivot.IteratorExp pivotIteratorExp = qvto.createIteratorExp(callExp);

		pivotIteratorExp.setName(callExp.getName());
		OCLExpression<EClassifier> source = callExp.getSource();
		if (source != null) {
			Object obj = doProcess(source);
			if (obj instanceof org.eclipse.ocl.pivot.OCLExpression) {
				org.eclipse.ocl.pivot.OCLExpression oclExpression = (org.eclipse.ocl.pivot.OCLExpression) obj;
				pivotIteratorExp.setOwnedSource(oclExpression);
			}
		}
		for (Variable<EClassifier, EParameter> iterator : callExp.getIterator()) {
			pivotIteratorExp.getOwnedIterators().add((org.eclipse.ocl.pivot.Variable) doProcess(iterator));
		}

		MappingQueue.getInstance().addIncompleteWork(callExp, pivotIteratorExp);
		return pivotIteratorExp;
	}

	@Override
	public Object visitVariable(Variable<EClassifier, EParameter> variable) {
		org.eclipse.ocl.pivot.Variable pivotVariable = qvto.createVariable(variable);
		MappingQueue.getInstance().addIncompleteWork(variable, pivotVariable);

		return pivotVariable;
	}

	@Override
	public Object visitPropertyCallExp(PropertyCallExp<EClassifier, EStructuralFeature> callExp) {
		org.eclipse.ocl.pivot.PropertyCallExp pivotPropertyCallExp = qvto.createPropertyCallExp();
		if (callExp.getSource() != null) {
			pivotPropertyCallExp.setOwnedSource((org.eclipse.ocl.pivot.OCLExpression) doProcess(callExp.getSource()));
		}
		MappingQueue.getInstance().addIncompleteWork(callExp, pivotPropertyCallExp);

		
		return pivotPropertyCallExp;
	}

	@Override
	public Object visitOperationCallExp(OperationCallExp<EClassifier, EOperation> callExp) {
		org.eclipse.ocl.pivot.OperationCallExp value = qvto.createOperationCallExp(callExp);
		OCLExpression<EClassifier> source = callExp.getSource();
		if (source != null) {

			Object obj = doProcess(source);
			if (obj instanceof org.eclipse.ocl.pivot.OCLExpression) {
				value.setOwnedSource((org.eclipse.ocl.pivot.OCLExpression) obj);
			}
		}

		for (OCLExpression<EClassifier> exp : callExp.getArgument()) {
			Object obj = doProcess(exp);
			if (obj instanceof org.eclipse.ocl.pivot.OCLExpression) {
				value.getOwnedArguments().add((org.eclipse.ocl.pivot.OCLExpression) obj);
			}
		}

		MappingQueue.getInstance().addIncompleteWork(callExp, value);

		return value;
	}

	@Override
	public Object visitVariableExp(VariableExp<EClassifier, EParameter> v) {

		org.eclipse.ocl.pivot.VariableExp variableExp = qvto.createVariableExp(v);

		Logger.getLogger().log(Logger.INFO, "Visiting Variable Exp => " + v, v);

	
		MappingQueue.getInstance().addIncompleteWork(v, variableExp);

//		variableExp.setReferredVariable((org.eclipse.ocl.pivot.VariableDeclaration) doProcess(v.getReferredVariable()));
		return variableExp;
	}

	@Override
	public Object visitMappingBody(MappingBody mappingBody) {
		org.eclipse.qvto.examples.pivot.qvtoperational.MappingBody pivotMappingBody = qvto.createMappingBody();

		for (org.eclipse.ocl.ecore.OCLExpression exp : mappingBody.getContent()) {
			Object obj = doProcess(exp);
			if (obj != null) {
				if (obj instanceof org.eclipse.ocl.pivot.OCLExpression) {
					pivotMappingBody.getContent().add((org.eclipse.ocl.pivot.OCLExpression) obj);
				}
			}
		}
		for (OCLExpression<EClassifier> initExp : mappingBody.getInitSection()) {
			pivotMappingBody.getInitSection().add((org.eclipse.ocl.pivot.OCLExpression) doProcess(initExp));
		}
		return pivotMappingBody;

	}

	@Override
	public Object visitObjectExp(ObjectExp objectExp) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ObjectExp pivotObjectExp = qvto.createObjectExp(objectExp);
		pivotObjectExp.setBody((org.eclipse.qvto.examples.pivot.qvtoperational.ConstructorBody) doProcess(objectExp.getBody()));
		MappingQueue.getInstance().addIncompleteWork(objectExp, pivotObjectExp);

		return pivotObjectExp;
	}

	@Override
	public Object visitAssignExp(AssignExp assignExp) {
		org.eclipse.qvto.examples.pivot.imperativeocl.AssignExp pivotAssignExp = qvto.createAssignExpOCL(assignExp);

		if (assignExp.getLeft() != null) {
			pivotAssignExp.setLeft((org.eclipse.ocl.pivot.OCLExpression) doProcess(assignExp.getLeft()));
		}
		if (assignExp.getValue() != null) {
			for (org.eclipse.ocl.ecore.OCLExpression exp : assignExp.getValue()) {
				Object obj = doProcess(exp);
				if (obj instanceof org.eclipse.ocl.pivot.OCLExpression) {
					pivotAssignExp.getValue().add((org.eclipse.ocl.pivot.OCLExpression) obj);
				}
			}

		}
		MappingQueue.getInstance().addIncompleteWork(assignExp, pivotAssignExp);

		return pivotAssignExp;
	}

	@SuppressWarnings("restriction")
	@Override
	public Object visitMappingOperation(MappingOperation mappingOperation) {

		org.eclipse.qvto.examples.pivot.qvtoperational.MappingOperation pivotOperation = qvto
				.createMappingOperation(mappingOperation);

		Logger.getLogger().log(Logger.INFO, "Mapping body of -> " + mappingOperation.getName(), mappingOperation);
		pivotOperation.setBody(
				(org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody) doProcess(mappingOperation.getBody()));
		// ====== Mapping the body for Mapping operation
		for (OCLExpression<EClassifier> exp : mappingOperation.getWhen()) {
			Logger.getLogger().log(Logger.INFO, "Mapping When of => " + mappingOperation.getName() + " => " + exp, exp);
			pivotOperation.setWhen((org.eclipse.ocl.pivot.OCLExpression) doProcess(exp));
		}
		if (mappingOperation.getWhere() instanceof BlockExp) {
			for (OCLExpression<EClassifier> exp : ((BlockExp) mappingOperation.getWhere()).getBody()) {
				Logger.getLogger().log(Logger.INFO, "Mapping Where of => " + mappingOperation.getName() + " => " + exp,
						exp);
				pivotOperation.setWhere((org.eclipse.ocl.pivot.OCLExpression) doProcess(exp));
			}
		}
		// ============================
		
		MappingQueue.getInstance().addIncompleteWork(mappingOperation, pivotOperation);

		return pivotOperation;
	}

	@Override
	public Object visitEntryOperation(EntryOperation entryOperation) {
		org.eclipse.qvto.examples.pivot.qvtoperational.EntryOperation pivotEntryOperation = qvto
				.createEntryOperation(entryOperation);
		pivotOperationalTransformation.setEntry(pivotEntryOperation);
		pivotEntryOperation.setBody((org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody) doProcess(entryOperation.getBody()));
		MappingQueue.getInstance().addIncompleteWork(entryOperation, pivotEntryOperation);

		return pivotEntryOperation;
	}

	@Override
	public Object visitOperationBody(OperationBody operationBody) {
		org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody pivotOperationBody = qvto.createOperationBody();
		for (org.eclipse.ocl.ecore.OCLExpression exp : operationBody.getContent()) {
			Object obj = doProcess(exp);
			if (obj != null) {
				pivotOperationBody.getContent().add((org.eclipse.ocl.pivot.OCLExpression) obj);
			}
		}

		return pivotOperationBody;
	}

	@Override
	public Object visitCollectionLiteralExp(CollectionLiteralExp<EClassifier> cl) {

		org.eclipse.ocl.pivot.CollectionLiteralExp pivotCollectionLiteralExp = qvto.createCollectionLiteralExp(cl);

		for (CollectionLiteralPart<EClassifier> parts : cl.getPart()) {
			Object obj = doProcess(parts);
			if (obj != null) {
				pivotCollectionLiteralExp.getOwnedParts().add((org.eclipse.ocl.pivot.CollectionLiteralPart) obj);
			}
		}

		if (cl.getKind().getName().equalsIgnoreCase("Set")) {
			pivotCollectionLiteralExp.setKind(org.eclipse.ocl.pivot.CollectionKind.SET);
		}

		MappingQueue.getInstance().addIncompleteWork(cl, pivotCollectionLiteralExp);

		return pivotCollectionLiteralExp;

	}

	@Override
	public Object visitModule(Module module) {
		java.util.@NonNull List<Operation> pivotOperations = pivotOperationalTransformation.getOwnedOperations();

		EList<EOperation> list = module.getEOperations();
		for (EOperation eOperation : list) {
			Operation tempOperation =  (Operation) doProcess((ImperativeOperation) eOperation);
			// Adding by reference into pivot OperationalTransformation
			pivotOperations.add(tempOperation);
			MappingQueue.getInstance().addOldMappingArgument(eOperation);
		}
		return pivotOperationalTransformation;
	}

	@Override
	public Object visitConstructorBody(ConstructorBody constructorBody) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ConstructorBody pivotConstructorBody = qvto.createConstructorBody();
		for (org.eclipse.ocl.ecore.OCLExpression exp : constructorBody.getContent()) {
			Object obj = doProcess(exp);
			if (obj != null) {
				pivotConstructorBody.getContent().add((org.eclipse.ocl.pivot.OCLExpression) obj);
			}
		}

		return pivotConstructorBody;
	}
		
	@Override
	public Object visitConstructor(Constructor constructor) {
		org.eclipse.qvto.examples.pivot.qvtoperational.Constructor pivotConstructor = qvto
				.createConstructor(constructor);
		pivotConstructor.setBody((org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody) doProcess(constructor.getBody()));
		return pivotConstructor;
	}
}
