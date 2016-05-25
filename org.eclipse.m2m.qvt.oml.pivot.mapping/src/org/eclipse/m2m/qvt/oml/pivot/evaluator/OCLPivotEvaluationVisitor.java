package org.eclipse.m2m.qvt.oml.pivot.evaluator;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil;
import org.eclipse.m2m.internal.qvt.oml.evaluator.EvaluationUtil;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtException;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl.BreakResult;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl.BreakingResult;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl.ContinueResult;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.ocl.EvaluationEnvironment;
import org.eclipse.ocl.EvaluationHaltedException;
import org.eclipse.ocl.pivot.NamedElement;
import org.eclipse.ocl.pivot.OCLExpression;
import org.eclipse.ocl.pivot.Operation;
import org.eclipse.ocl.pivot.Property;
import org.eclipse.ocl.pivot.PropertyCallExp;
import org.eclipse.ocl.pivot.Type;
import org.eclipse.ocl.pivot.Variable;
import org.eclipse.ocl.pivot.VariableDeclaration;
import org.eclipse.ocl.pivot.VariableExp;
import org.eclipse.ocl.pivot.internal.manager.PivotMetamodelManager;
import org.eclipse.ocl.pivot.utilities.ClassUtil;
import org.eclipse.ocl.pivot.utilities.OCL;
import org.eclipse.ocl.pivot.values.InvalidValueException;
import org.eclipse.qvtd.pivot.qvtimperative.evaluation.QVTiEnvironmentFactory;
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


	QVTiEnvironmentFactory qvTiEnvironmentFactory = new QVTiEnvironmentFactory(OCL.NO_PROJECTS, null);

	final Map<String, Object> objects = new LinkedHashMap<String, Object>();


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

		SwitchAltExpResult result = new SwitchAltExpResult();
		result.myCondition = visitExpression(astNode.getCondition());
		if (Boolean.TRUE.equals(result.myCondition)) {
			result.myResult = visitExpression(astNode.getBody());
		}
		return result;
		//		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitAssertExp(AssertExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}
	public Object visitExpression(OCLExpression expression) {
		try {
			return expression.accept(this);
		} catch (EvaluationHaltedException e) {
			// evaluation stopped on demand, propagate further
			throw e;
		}
	}
	//	@Override
	//	public Object visitOperationCallExp(org.eclipse.ocl.pivot.@NonNull OperationCallExp operationCallExp)
	//	{
	//		if (isCanceled()) {
	//			throw new EvaluationHaltedException("Canceled");
	//		}
	//		Operation apparentOperation = operationCallExp.getReferredOperation();
	//		assert apparentOperation != null;
	//		//
	//		//	Resolve source value
	//		//
	// 		Object sourceValue;
	//		OCLExpression source = operationCallExp.getOwnedSource();
	//		boolean isValidating = apparentOperation.isIsValidating();
	//		if (isValidating) {
	//			try {
	//				sourceValue = source.accept(undecoratedVisitor);
	//			}
	//			catch (EvaluationHaltedException e) {
	//				throw e;
	//			}
	//			catch (InvalidValueException e) {
	//				sourceValue = e;	// FIXME ?? propagate part of environment
	//			}
	//		}
	//		else {
	//			sourceValue = source.accept(undecoratedVisitor);
	//		}
	//		return sourceValue;
	//		
	//	}

	@Override
	public Object visitPropertyCallExp(@NonNull PropertyCallExp propertyCallExp) {
		Property referredProperty = propertyCallExp.getReferredProperty();

		if (referredProperty != null) {
			OCLExpression source = propertyCallExp.getOwnedSource();
			if (source != null) {
				Object sourceValue = source.accept(this);
				return  sourceValue;//context.internalExecuteNavigationCallExp(propertyCallExp, referredProperty, sourceValue);
			}
		}
		return null;
	}
	@Override
	public Object visitVariableExp(@NonNull VariableExp variableExp) {
		//System.out.println(variableExp.getName());

		@Nullable
		Object value = variableExp.getReferredVariable().accept(this);
		if(value!=null)
		{
			return value;
		}
		if(objects.get(variableExp.getName())!=null)
		{
			Object obj= objects.get(variableExp.getName());
			return obj;
//			System.out.println();
		}

		return variableExp;
	}


	@Override
	public @Nullable Object visitAssignExp(AssignExp astNode) {
		// TODO Auto-generated method stub
		Object exprValue = null;
		for (OCLExpression exp : astNode.getValue()) {

			exprValue = visitExpression(exp);
		}
		OCLExpression left = astNode.getLeft();
		Object leftValue = visitExpression(left);


		if(left instanceof PropertyCallExp)
		{
			PropertyCallExp propertyCallExp=(PropertyCallExp)left;
			Property referredProperty = propertyCallExp.getReferredProperty();
			//propertyCallExp.setName((String) exprValue);
			OCLExpression ownedSource = propertyCallExp.getOwnedSource();
			setValue(ownedSource,exprValue,propertyCallExp);
		}

		return exprValue;
	}

	private void setValue(OCLExpression left, Object exprValue, PropertyCallExp propertyCallExp) {
		// TODO Auto-generated method stub

		Object leftExpression=left.accept(this);
		if(leftExpression instanceof VariableExp)
		{
			VariableExp variable= (VariableExp)leftExpression;
			String oldValue=variable.getName();
			variable.setName((String) exprValue);
			//	System.out.println();
			if(objects.get(propertyCallExp.getName())==null)
			{
				objects.put(propertyCallExp.getName(), variable);
			}
			else
			{
				objects.replace(propertyCallExp.getName(), variable);
			}
			if(objects.get(variable.getName())==null)
			{
				objects.put(oldValue, variable);
			}
			else
			{
				objects.replace(oldValue, variable);
			}
		}
		//leftExpression=left.accept(this);
		//Property referredProperty = propertyCallExp.getReferredProperty();


	}

	@Override
	public @Nullable Object visitBlockExp(BlockExp astNode) {
		// TODO Auto-generated method stub
		return visitBlockExpImpl(astNode.getBody(), astNode.eContainer() instanceof ImperativeOperation);
		//	return visiting(astNode);
	}
	private Object visitBlockExpImpl(EList<OCLExpression> expList, boolean isInImperativeOper) {
		List<String> scopeVars = null;

		Object result = null;

		for (OCLExpression exp : expList) {
			if((exp instanceof VariableInitExp) && !isInImperativeOper) {
				if(scopeVars == null) {
					scopeVars = new LinkedList<String>();
				}
				VariableInitExp varInitExp = (VariableInitExp) exp;
				scopeVars.add(varInitExp.getName());
			}

			result = visitExpression(exp);
			if(result instanceof BreakingResult) {
				break;
			}
			// Return null, unless control flow is broken (by break, continue or return).
			// When control flow is broken, propagate this upward in the AST.
			result = null;
		}
		return result;
	}

	public static class BreakResult implements BreakingResult {
		BreakResult() { }
	}

	protected final static BreakResult BREAK = new BreakResult();
	@Override
	public @Nullable Object visitBreakExp(BreakExp astNode) {
		// TODO Auto-generated method stub
		return BREAK;
	}

	@Override
	public @Nullable Object visitCatchExp(CatchExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitComputeExp(ComputeExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}
	public static class ContinueResult implements BreakingResult {
		ContinueResult() { }
	}

	protected final static ContinueResult CONTINUE = new ContinueResult();

	@Override
	public @Nullable Object visitContinueExp(ContinueExp astNode) {
		// TODO Auto-generated method stub
		return CONTINUE;
	}

	@Override
	public @Nullable Object visitDictLiteralExp(DictLiteralExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitDictLiteralPart(DictLiteralPart astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitDictionaryType(DictionaryType astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitForExp(ForExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitImperativeExpression(ImperativeExpression astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitImperativeIterateExp(ImperativeIterateExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitImperativeLoopExp(ImperativeLoopExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitInstantiationExp(InstantiationExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitListLiteralExp(ListLiteralExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitListType(ListType astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitLogExp(LogExp astNode) {
		// TODO Auto-generated method stub

		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitOrderedTupleLiteralExp(OrderedTupleLiteralExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitOrderedTupleLiteralPart(OrderedTupleLiteralPart astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitOrderedTupleType(OrderedTupleType astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitRaiseExp(RaiseExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitReturnExp(ReturnExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	private static class SwitchAltExpResult {
		public Object myCondition;
		public Object myResult;
	}
	@Override
	public @Nullable Object visitSwitchExp(SwitchExp astNode) {
		// TODO Auto-generated method stub

		for (AltExp altExp : astNode.getAlternativePart()) {
			Object altResult = visitExpression(altExp);	
			if (altResult instanceof SwitchAltExpResult) {
				if (Boolean.TRUE.equals(((SwitchAltExpResult) altResult).myCondition)) {
					return ((SwitchAltExpResult) altResult).myResult;
				}
			}
			else if (Boolean.TRUE.equals(altResult)) {
				return null;
			}
		}
		OCLExpression elsePart = astNode.getElsePart();
		if (elsePart != null) {
			return visitExpression(elsePart);
		}
		return null;
		//return visiting(astNode);
	}

	@Override
	public @Nullable Object visitTryExp(TryExp tryExp) {
		// TODO Auto-generated method stub

		try {
			return visitBlockExpImpl(tryExp.getTryBody(), tryExp.eContainer() instanceof ImperativeOperation);
		}
		catch (QvtException exception) {
			boolean processed = false;

			OUTERMOST: for (CatchExp catchExp : tryExp.getExceptClause()) {
				for (Type excType : catchExp.getException()) {

				}
				if (catchExp.getException().isEmpty()) { // catch all

					break OUTERMOST;
				}
			}

			if (!processed) {
				throw exception;
			}
		}

		return null;
		//return visiting(astNode);
	}

	@Override
	public @Nullable Object visitTypedef(Typedef astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitUnlinkExp(UnlinkExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitUnpackExp(UnpackExp astNode) {
		// TODO Auto-generated method stub
		return visiting(astNode);
	}

	@Override
	public @Nullable Object visitVariableInitExp(VariableInitExp variableInitExp) {
		// TODO Auto-generated method stub
		Variable referredVariable = variableInitExp.getReferredVariable();
		if(referredVariable!=null)
		{
			OCLExpression initExpression = referredVariable.getOwnedInit();
			Object value = null;
			if(initExpression != null) {
				value = visitExpression(initExpression);
			} 

			return variableInitExp.isWithResult() ? value : null;
		}
		return null;
		//	return visiting(astNode);
	}

	@Override
	public @Nullable Object visitWhileExp(WhileExp whileExp) {
		// TODO Auto-generated method stub

		Object result = null;
		while (true) {
			Object condition = visitExpression(whileExp.getCondition());
			if (Boolean.TRUE.equals(condition)) {
				result = visitExpression(whileExp.getBody());

				if(result instanceof BreakingResult) {
					// Control flow is being broken (break, continue, or return).

					if(result instanceof BreakResult) {
						// Result must be null, unless it comes from a return statement.
						result = null;
						break;
					}
					if(result instanceof ContinueExp) {
						// Instead of breaking out of the loop, continue with the next iteration.
						result = null;
						continue;
					}
					break;
				}
			} else {
				break;
			}
		}

		return result;
		//		return visiting(astNode);
	}


}
