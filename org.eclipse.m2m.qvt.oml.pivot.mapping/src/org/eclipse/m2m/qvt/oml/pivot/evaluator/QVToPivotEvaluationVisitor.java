package org.eclipse.m2m.qvt.oml.pivot.evaluator;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.m2m.internal.qvt.oml.NLS;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.parser.QvtOperationalParserUtil;
import org.eclipse.m2m.internal.qvt.oml.ast.parser.QvtOperationalUtil;
import org.eclipse.m2m.internal.qvt.oml.evaluator.EvaluationMessages;
import org.eclipse.m2m.internal.qvt.oml.evaluator.EvaluationUtil;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtAssertionFailed;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtRuntimeException;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl.BreakingResult;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl.OperationCallResult;
import org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeCallExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingCallExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter;
import org.eclipse.m2m.internal.qvt.oml.stdlib.CallHandler;
import org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util.TraditionalToPivotMapping;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.pivot.Element;
import org.eclipse.ocl.pivot.evaluation.EvaluationEnvironment;
import org.eclipse.ocl.pivot.internal.evaluation.OCLEvaluationVisitor;
import org.eclipse.ocl.pivot.utilities.OCL;
import org.eclipse.qvtd.pivot.qvtimperative.evaluation.QVTiEnvironmentFactory;
import org.eclipse.qvto.examples.pivot.qvtoperational.util.QVTOperationalAdapterFactory;

@SuppressWarnings("restriction")
public  class QVToPivotEvaluationVisitor  extends OCLPivotEvaluationVisitor
{

	QVTiEnvironmentFactory qvTiEnvironmentFactory = new QVTiEnvironmentFactory(OCL.NO_PROJECTS, null);
	

	public QVToPivotEvaluationVisitor(BasicQVToExecutor basicQVToExecutor) {
		super(basicQVToExecutor);

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
	public Object  visitHelper( org.eclipse.qvto.examples.pivot.qvtoperational.Helper helper) {
	//	doProcess(helper.getBody());
	//	doProcessAll(helper.getOwnedParameters());
		return null;
	}


	public Object visitImperativeCallExp( org.eclipse.qvto.examples.pivot.qvtoperational.ImperativeCallExp astNode) {
		
		//super.visitImperativeCallExp(astNode);
		doProcess(astNode.getOwnedSource());
		return null;
	}
	public Object  visitImperativeOperation( org.eclipse.qvto.examples.pivot.qvtoperational.ImperativeOperation astNode) {
		return visiting(astNode);
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
		doProcess(astNode.getBody());
		return null;
	}
	public Object  visitMappingParameter( org.eclipse.qvto.examples.pivot.qvtoperational.MappingParameter astNode) {
		//System.out.println(astNode.getName());
		String s=astNode.getName();
		return null;//visiting(astNode);
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



		//		return visiting(astNode);
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
