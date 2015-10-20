package org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl;
import org.eclipse.m2m.internal.qvt.oml.expressions.EntryOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Helper;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.pivot.Operation;
import org.eclipse.ocl.pivot.PivotFactory;
import org.eclipse.qvto.examples.pivot.qvtoperational.QVTOperationalFactory;

public class TraditionalToPivotMappingVisitorImpl extends QvtOperationalEvaluationVisitorImpl
		implements TraditionalToPivotMappingVisitor {

	private QVToFacade qvto;
	private org.eclipse.qvto.examples.pivot.qvtoperational.OperationalTransformation pivotOperationalTransformation;

	public TraditionalToPivotMappingVisitorImpl(QVToFacade qvto, QvtOperationalEnv env, QvtOperationalEvaluationEnv evalEnv) {
		super(env, evalEnv);
		this.qvto = qvto;
		pivotOperationalTransformation = QVTOperationalFactory.eINSTANCE.createOperationalTransformation();
	}

	@Override
	public Object visitHelper(Helper helper) {
		return qvto.createHelper(helper);
	}

	@Override
	public Object visitMappingOperation(MappingOperation mappingOperation) {
		return qvto.createMappingOperation(mappingOperation);
	}

	@Override
	public Object visitEntryOperation(EntryOperation entryOperation) {
		org.eclipse.qvto.examples.pivot.qvtoperational.EntryOperation pivotEntryOperation = qvto.createEntryOperation(entryOperation);
		pivotOperationalTransformation.setEntry(pivotEntryOperation);
		return pivotEntryOperation;
	}
	@Override
	public Object visitOperationBody(OperationBody operationBody)
	{
		org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody pivotOperationBody = QVTOperationalFactory.eINSTANCE.createOperationBody();
		for (OCLExpression<EClassifier> exp : operationBody.getContent())
		{
			pivotOperationBody.getContent();
			exp.accept(this);
		 }
		return null;
		
	}
	@Override
    public Object visitOperationCallExp(OperationCallExp<EClassifier, EOperation> operationCallExp) {
		org.eclipse.ocl.pivot.OperationCallExp value = PivotFactory.eINSTANCE.createOperationCallExp();
		OCLExpression<EClassifier> list = operationCallExp.getSource();
//  	operationCallExp.accept(this);
//		Object ret = list.accept(this);
		org.eclipse.ocl.pivot.OCLExpression asSource;
		
//		asCallExp.setOwnedSource(asSource);
		return null;
	}
	
	@Override
	public Object visitModule(Module module) {
		java.util.@NonNull List<Operation> pivotOperations = pivotOperationalTransformation.getOwnedOperations();

		EList<EOperation> list = module.getEOperations();
		for (EOperation eOperation : list) {
			
			Operation tempOperation = (Operation) ((ImperativeOperation) eOperation).accept(this);
			((ImperativeOperation)eOperation).getBody().accept(this);
			//Adding by reference into pivot OperationalTransformation
			pivotOperations.add(tempOperation);
		}
		//pivotOperationalTransformation.accept(new QVTOperationalToStringVisitor(new StringBuilder()));
		return pivotOperationalTransformation;
	}

}