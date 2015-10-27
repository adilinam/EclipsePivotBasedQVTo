package org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.emf.util.Logger;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl;
import org.eclipse.m2m.internal.qvt.oml.expressions.EntryOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Helper;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.BlockExp;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.pivot.Operation;
import org.eclipse.qvto.examples.pivot.qvtoperational.MappingCallExp;
import org.eclipse.qvto.examples.pivot.qvtoperational.QVTOperationalFactory;

public class TraditionalToPivotMappingVisitorImpl extends QvtOperationalEvaluationVisitorImpl
		implements TraditionalToPivotMappingVisitor {

	private QVToFacade qvto;
	private org.eclipse.qvto.examples.pivot.qvtoperational.OperationalTransformation pivotOperationalTransformation;

	public TraditionalToPivotMappingVisitorImpl(QVToFacade qvto, QvtOperationalEnv env,
			QvtOperationalEvaluationEnv evalEnv) {
		super(env, evalEnv);
		this.qvto = qvto;
		pivotOperationalTransformation = QVTOperationalFactory.eINSTANCE.createOperationalTransformation();
	}

	@Override
	public Object visitHelper(Helper helper) {
		org.eclipse.qvto.examples.pivot.qvtoperational.Helper pivotHelper = qvto.createHelper(helper);
		((ImperativeOperation)helper).getBody().accept(this);
		return pivotHelper;
	}
	
	@Override
	public Object visitOperationCallExp(OperationCallExp<EClassifier, EOperation> callExp) {
		//org.eclipse.ocl.pivot.OperationCallExp value = PivotFactory.eINSTANCE.createOperationCallExp();
		//org.eclipse.ocl.pivot.ExpressionInOCL pivotOCLExpression =  qvto.createOperationCallExp(callExp.toString());
		//Logger.getLogger().log(Logger.INFO,"Pivot based expression: " + pivotOCLExpression, pivotOCLExpression);
				OCLExpression<EClassifier> source = callExp.getSource();
		        if (source != null) {
		        	source.accept(this); //FIXME create visitVariableExp() for this
		        }

		        for (OCLExpression<EClassifier> exp : callExp.getArgument()) {
		        	source.accept(this);
		        }
		        EOperation referredOperation = callExp.getReferredOperation();
		        if (referredOperation instanceof org.eclipse.ocl.utilities.Visitable) {
		        	// TODO -
		        	// Should not visit referenced operation, as thesemight com from different top container (Module)
		        	// Should strictly traverse on containment basis, no need to track processed nodes
		            //doProcess((Visitable) referredOperation, callExp);
		        }
		        // FIXME: Should return pivot source element
		        return null;
	}

	public Object visitMappingCallExp(MappingCallExp mappingCallExp) {
		// visitOperationCallExp(mappingCallExp);
		return null;
	}

	@SuppressWarnings("restriction")
	@Override
	public Object visitMappingOperation(MappingOperation mappingOperation) {
		org.eclipse.qvto.examples.pivot.qvtoperational.MappingOperation pivotOperation = qvto
				.createMappingOperation(mappingOperation);
		Logger.getLogger().log(Logger.INFO, "Mapping body of -> "+mappingOperation.getName(), mappingOperation);
		// ====== Mapping the body for Mapping operation
		for (OCLExpression<EClassifier> exp : mappingOperation.getWhen()) {
			Logger.getLogger().log(Logger.INFO, "Mapping When of => "+mappingOperation.getName()+" => "+exp, exp);
			exp.accept(this);
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
		org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody pivotOperationBody = QVTOperationalFactory.eINSTANCE
				.createOperationBody();
		for (OCLExpression<EClassifier> exp : operationBody.getContent()) {
			pivotOperationBody.getContent();
			exp.accept(this);
		}
		return null;
	}

	@Override
	public Object visitModule(Module module) {
		java.util.@NonNull List<Operation> pivotOperations = pivotOperationalTransformation.getOwnedOperations();

		EList<EOperation> list = module.getEOperations();
		for (EOperation eOperation : list) {

			Operation tempOperation = (Operation) ((ImperativeOperation) eOperation).accept(this);
			// ((ImperativeOperation)eOperation).getBody().accept(this);
			// Adding by reference into pivot OperationalTransformation
			pivotOperations.add(tempOperation);
		}
		// pivotOperationalTransformation.accept(new
		// QVTOperationalToStringVisitor(new StringBuilder()));
		return pivotOperationalTransformation;
	}

}
