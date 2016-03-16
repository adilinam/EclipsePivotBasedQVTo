package org.eclipse.m2m.qvt.oml.pivot.evaluator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ocl.EvaluationHaltedException;
import org.eclipse.ocl.pivot.OCLExpression;
import org.eclipse.ocl.pivot.internal.manager.PivotMetamodelManager;
import org.eclipse.ocl.pivot.utilities.ClassUtil;
import org.eclipse.qvto.examples.pivot.imperativeocl.AltExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.AssertExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.AssignExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.BlockExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.BreakExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.CatchExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.ComputeExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.ContinueExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.DictLiteralExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.DictLiteralPart;
import org.eclipse.qvto.examples.pivot.imperativeocl.DictionaryType;
import org.eclipse.qvto.examples.pivot.imperativeocl.ForExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.ImperativeExpression;
import org.eclipse.qvto.examples.pivot.imperativeocl.ImperativeIterateExp;import org.eclipse.qvto.examples.pivot.imperativeocl.ImperativeLoopExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.InstantiationExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.ListLiteralExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.ListType;
import org.eclipse.qvto.examples.pivot.imperativeocl.LogExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.OrderedTupleLiteralExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.OrderedTupleLiteralPart;
import org.eclipse.qvto.examples.pivot.imperativeocl.OrderedTupleType;
import org.eclipse.qvto.examples.pivot.imperativeocl.RaiseExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.ReturnExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.SwitchExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.TryExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.Typedef;
import org.eclipse.qvto.examples.pivot.imperativeocl.UnlinkExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.UnpackExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.VariableInitExp;
import org.eclipse.qvto.examples.pivot.imperativeocl.WhileExp;
import org.eclipse.qvto.examples.pivot.qvtoperational.OperationalTransformation;

public class OCLPivotEvaluationVisitor extends AbstractQVToPivotVisitorImpl  {


	

	protected OCLPivotEvaluationVisitor(BasicQVToExecutor basicQVToExecutor) 
	{
		super(basicQVToExecutor);

		}

	private @Nullable OperationalTransformation loadTransformation(@NonNull PivotMetamodelManager metamodelManager) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitAltExp(AltExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitAssertExp(AssertExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}
	public Object visitExpression(OCLExpression expression) {
        try {
            return expression.accept(this);
        } catch (EvaluationHaltedException e) {
        	// evaluation stopped on demand, propagate further
        	throw e;
        }
	}

	@Override
	public @Nullable Object visitAssignExp(AssignExp astNode) {
		// TODO Auto-generated method stub
		Object exprValue = null;
		for (OCLExpression exp : astNode.getValue()) {
			
			exprValue = visitExpression(exp);
		}
		return null;
	}

	@Override
	public @Nullable Object visitBlockExp(BlockExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitBreakExp(BreakExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitCatchExp(CatchExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitComputeExp(ComputeExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitContinueExp(ContinueExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitDictLiteralExp(DictLiteralExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitDictLiteralPart(DictLiteralPart astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitDictionaryType(DictionaryType astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitForExp(ForExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitImperativeExpression(ImperativeExpression astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitImperativeIterateExp(ImperativeIterateExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitImperativeLoopExp(ImperativeLoopExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitInstantiationExp(InstantiationExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitListLiteralExp(ListLiteralExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitListType(ListType astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitLogExp(LogExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitOrderedTupleLiteralExp(OrderedTupleLiteralExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitOrderedTupleLiteralPart(OrderedTupleLiteralPart astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitOrderedTupleType(OrderedTupleType astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitRaiseExp(RaiseExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitReturnExp(ReturnExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitSwitchExp(SwitchExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitTryExp(TryExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitTypedef(Typedef astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitUnlinkExp(UnlinkExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitUnpackExp(UnpackExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitVariableInitExp(VariableInitExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Object visitWhileExp(WhileExp astNode) {
		// TODO Auto-generated method stub
		return null;
	}


}
