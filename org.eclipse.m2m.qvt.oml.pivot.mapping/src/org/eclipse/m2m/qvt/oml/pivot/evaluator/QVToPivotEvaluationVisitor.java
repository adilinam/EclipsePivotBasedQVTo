package org.eclipse.m2m.qvt.oml.pivot.evaluator;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl.BreakingResult;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl.OperationCallResult;
import org.eclipse.m2m.qvt.oml.pivot.evaluator.OCLPivotEvaluationVisitor.BreakResult;
import org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util.TraditionalToPivotMapping;
import org.eclipse.ocl.pivot.Element;
import org.eclipse.ocl.pivot.Parameter;
import org.eclipse.ocl.pivot.TypedElement;
import org.eclipse.ocl.pivot.evaluation.EvaluationEnvironment;
import org.eclipse.ocl.pivot.internal.evaluation.OCLEvaluationVisitor;
import org.eclipse.qvto.examples.pivot.qvtoperational.util.QVTOperationalAdapterFactory;

@SuppressWarnings("restriction")


public  class QVToPivotEvaluationVisitor  extends OCLPivotEvaluationVisitor
{
	


	BasicQVToExecutor basicQVToExecutor;
	public QVToPivotEvaluationVisitor(BasicQVToExecutor basicQVToExecutor) {
		super(basicQVToExecutor);
		this.basicQVToExecutor=basicQVToExecutor;
		
	}

	public Object visiting(Object astNode) {
//		throw new UnsupportedOperationException("Unsupported " + getClass().getSimpleName() + " for " + ((EObject)astNode).eClass().getName());
		System.err.println("Unsupported " + getClass().getSimpleName() + " for " + ((EObject)astNode).eClass().getName());
		return null;
	}

	public Object  visitConstructor( org.eclipse.qvto.examples.pivot.qvtoperational.Constructor astNode) {
		return visiting(astNode);
	}
	public Object  visitConstructorBody( org.eclipse.qvto.examples.pivot.qvtoperational.ConstructorBody astNode) {
		
		doProcessAll(astNode.getContent());
		
		return null;// visiting(astNode);
	}
	public Object  visitContextualProperty( org.eclipse.qvto.examples.pivot.qvtoperational.ContextualProperty astNode) {
		return visiting(astNode);
	}
	public Object  visitDummyRelation( org.eclipse.qvto.examples.pivot.qvtoperational.DummyRelation astNode) {
		return visiting(astNode);
	}
	public Object  visitDummyRelationDomain( org.eclipse.qvto.examples.pivot.qvtoperational.DummyRelationDomain astNode) {
		return visiting(astNode);
	}
	public Object  visitDummyRelationalTransformation( org.eclipse.qvto.examples.pivot.qvtoperational.DummyRelationalTransformation astNode) {
		return visiting(astNode);
	}
	public Object  visitEntryOperation( org.eclipse.qvto.examples.pivot.qvtoperational.EntryOperation astNode) {
		return visiting(astNode);
	}
	public Object  visitHelper( org.eclipse.qvto.examples.pivot.qvtoperational.Helper astNode) {
		
		doProcess(astNode.getBody());
			doProcessAll(astNode.getOwnedParameters());
		return null;
	}
	public Object visitImperativeCallExp( org.eclipse.qvto.examples.pivot.qvtoperational.ImperativeCallExp astNode) {
		doProcess(astNode.getOwnedSource());
		return null;//visiting(astNode);
	}
	public Object  visitImperativeOperation( org.eclipse.qvto.examples.pivot.qvtoperational.ImperativeOperation astNode) {
		@NonNull
		List<Parameter> parameters = astNode.getOwnedParameters();
		for(TypedElement parameter:parameters)
		{
			basicQVToExecutor.add(parameter,parameter);
		//	System.out.println();
		}
		
		return null;//visiting(astNode);
	}
	public Object  visitLibrary( org.eclipse.qvto.examples.pivot.qvtoperational.Library astNode) {
		return visiting(astNode);
	}
	public Object visitMappingBody( org.eclipse.qvto.examples.pivot.qvtoperational.MappingBody astNode) {
		
		doProcessAll(astNode.getContent());
		return null;
	}
	public Object  visitMappingCallExp( org.eclipse.qvto.examples.pivot.qvtoperational.MappingCallExp astNode) {
		if(astNode.getOwnedSource()!=null)
		{
			doProcess(astNode.getOwnedSource());
		}

		return null;
	}
	public Object  visitMappingOperation( org.eclipse.qvto.examples.pivot.qvtoperational.MappingOperation astNode) {
		visitImperativeOperation(astNode);
		doProcess(astNode.getBody());
		return null;
	}
	public Object  visitMappingParameter( org.eclipse.qvto.examples.pivot.qvtoperational.MappingParameter astNode) {
		return visiting(astNode);
	}
	public Object  visitModelParameter( org.eclipse.qvto.examples.pivot.qvtoperational.ModelParameter astNode) {
		return visiting(astNode);
	}
	public Object  visitModelType( org.eclipse.qvto.examples.pivot.qvtoperational.ModelType astNode) {
		return visiting(astNode);
	}
	public Object  visitModule( org.eclipse.qvto.examples.pivot.qvtoperational.Module astNode) {
		
		return visiting(astNode);
	}
	public Object  visitModuleImport( org.eclipse.qvto.examples.pivot.qvtoperational.ModuleImport astNode) {
		return visiting(astNode);
	}
	public Object  visitObjectExp( org.eclipse.qvto.examples.pivot.qvtoperational.ObjectExp astNode) {
		doProcess(astNode.getBody());
		return null;
	}
	public Object  visitOperationBody(  org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody operationBody) {
		
			Object result = null;
			for (org.eclipse.ocl.pivot.OCLExpression exp : operationBody.getContent()) {
				result = visitExpression(exp);

				// If control flow was broken (by means of a return statement,
				// stop executing the next lines and return this result.
				if(result instanceof BreakingResult) {
					if(result instanceof OperationCallResult) {
						result = ((OperationCallResult)result).myResult;
					}
					else {
						result = null;
					}
					break;
				}
			}
			org.eclipse.qvto.examples.pivot.qvtoperational.ImperativeOperation operation = operationBody.getOperation();

			if(operation.getType() == getStandardLibrary().getOclVoidType()) {
				result = null;
			}


			return result;
	}
	public Object  visitOperationalTransformation( org.eclipse.qvto.examples.pivot.qvtoperational.OperationalTransformation astNode) {
	doProcessAll(astNode.getOwnedOperations());
		
		return null;
	}
	public Object  visitResolveExp( org.eclipse.qvto.examples.pivot.qvtoperational.ResolveExp astNode) {
		return visiting(astNode);
	}
	public Object  visitResolveInExp( org.eclipse.qvto.examples.pivot.qvtoperational.ResolveInExp astNode) {
		return visiting(astNode);
	}
	public Object  visitTag( org.eclipse.qvto.examples.pivot.qvtoperational.Tag astNode) {
		return visiting(astNode);
	}
	public Object visitVarParameter( org.eclipse.qvto.examples.pivot.qvtoperational.VarParameter astNode) {
		return visiting(astNode);
	}

}
