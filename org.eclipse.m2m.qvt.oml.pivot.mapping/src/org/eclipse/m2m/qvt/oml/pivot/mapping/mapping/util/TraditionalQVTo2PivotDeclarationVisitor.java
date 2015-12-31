package org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.m2m.internal.qvt.oml.cst.IntermediateClassDefCS;
import org.eclipse.m2m.internal.qvt.oml.expressions.Constructor;
import org.eclipse.m2m.internal.qvt.oml.expressions.ConstructorBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.ContextualProperty;
import org.eclipse.m2m.internal.qvt.oml.expressions.EntryOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Helper;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeCallExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Library;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingCallExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingParameter;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelParameter;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelType;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport;
import org.eclipse.m2m.internal.qvt.oml.expressions.ObjectExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.ResolveInExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.AltExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.AssertExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.AssignExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.BlockExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.BreakExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.CatchExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ComputeExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ContinueExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.DictLiteralExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.DictLiteralPart;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ForExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ImperativeIterateExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.InstantiationExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.LogExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.OrderedTupleLiteralExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.OrderedTupleLiteralPart;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.RaiseExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ReturnExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.SeverityKind;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.SwitchExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.TryExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.UnlinkExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.UnpackExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.VariableInitExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.WhileExp;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.pivot.Element;
import org.eclipse.ocl.pivot.OCLExpression;
import org.eclipse.ocl.pivot.Parameter;
import org.eclipse.ocl.pivot.PivotFactory;
import org.eclipse.ocl.pivot.Type;
import org.eclipse.ocl.types.CollectionType;
import org.eclipse.ocl.types.TupleType;
import org.eclipse.ocl.types.TypeType;
import org.eclipse.qvto.examples.pivot.imperativeocl.ImperativeOCLFactory;
import org.eclipse.qvto.examples.pivot.qvtoperational.QVTOperationalFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import simpleuml.Classifier;

@SuppressWarnings("restriction")
public class TraditionalQVTo2PivotDeclarationVisitor extends TraditionalOCL2PivotDeclarationVisitor
{
	protected static class OCLEcoreSwitch extends AbstractEcoreSwitch
	{
		public OCLEcoreSwitch(@NonNull TraditionalToPivotMapping converter) {
			super(converter);
		}

		@Override
		public Element caseEPackage(EPackage object) {
			return super.caseEPackage(object);
		}
	}

	public TraditionalQVTo2PivotDeclarationVisitor(TraditionalToPivotMapping converter) {
		super(converter, new OCLEcoreSwitch(converter));
	}

	@Override
	public Object visitAltExp(AltExp astNode) {
		// TODO Auto-generated method stub
		org.eclipse.qvto.examples.pivot.imperativeocl.AltExp pivotElement =
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createAltExp());
		pivotElement.setBody(doProcess(OCLExpression.class, astNode.getBody()));
		pivotElement.setCondition(doProcess(OCLExpression.class, astNode.getCondition()));
		return pivotElement;
	}

	@Override
	public Object visitAssertExp(AssertExp astNode) {
		org.eclipse.qvto.examples.pivot.imperativeocl.AssertExp pivotElement = 
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createAssertExp());
		pivotElement.setAssertion(doProcess(OCLExpression.class, astNode.getAssertion()));
		pivotElement.setLog(doProcess(org.eclipse.qvto.examples.pivot.imperativeocl.LogExp.class, astNode.getLog()));
		//pivotElement.setSeverity(doProcess(org.eclipse.qvto.examples.pivot.imperativeocl.SeverityKind.class, astNode.getSeverity()));
		return pivotElement;
	}

	public Object visitAssignExp(AssignExp assignExp) {
		org.eclipse.qvto.examples.pivot.imperativeocl.AssignExp pivotElement = ImperativeOCLFactory.eINSTANCE.createAssignExp();
		converter.addCreated(assignExp, pivotElement);
		pivotElement.setLeft(doProcess(org.eclipse.ocl.pivot.OCLExpression.class, assignExp.getLeft()));
		pivotElement.getValue().addAll(doProcessAll(org.eclipse.ocl.pivot.OCLExpression.class, assignExp.getValue()));

		//pivotAssignExp.setType(metamodelManager.getASOfEcore(Type.class, assignExp.getType())); // FIXME Bug 479445
		return pivotElement;
	}

	@Override
	public Object visitBlockExp(BlockExp astNode) {
		org.eclipse.qvto.examples.pivot.imperativeocl.BlockExp pivotElement = 
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createBlockExp());
		pivotElement.getBody().addAll(doProcessAll(OCLExpression.class, astNode.getBody()));
		return pivotElement;
	}

	@Override
	public Object visitBreakExp(BreakExp astNode) {
		org.eclipse.qvto.examples.pivot.imperativeocl.BreakExp pivotElement =
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createBreakExp());
		//...
		return pivotElement;
	}

	@Override
	public Object visitCatchtExp(CatchExp astNode) {
		// TODO Auto-generated method stub
		org.eclipse.qvto.examples.pivot.imperativeocl.CatchExp pivotElement =
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createCatchExp());
		pivotElement.getBody().addAll(doProcessAll(OCLExpression.class, astNode.getBody()));
		
		//pivotElement.getException().addAll(doProcessAll(Type.class, astNode.getException()));
		return pivotElement;
	}

	@Override
	public Object visitComputeExp(ComputeExp astNode) {
		// TODO Auto-generated method stub
		return super.visitComputeExp(astNode);
	}

	public Object visitConstructor(Constructor astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.Constructor pivotElement =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createConstructor());
		pivotElement.setName(astNode.getName());
		pivotElement.setBody(doProcess(org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody.class, astNode.getBody()));
		return pivotElement;
	}

	public Object visitConstructorBody(ConstructorBody astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ConstructorBody pivotElement =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createConstructorBody());
		pivotElement.getContent().addAll(doProcessAll(org.eclipse.ocl.pivot.OCLExpression.class, astNode.getContent()));
		pivotElement.getVariable().addAll(doProcessAll(org.eclipse.ocl.pivot.Variable.class, astNode.getVariable()));
		return pivotElement;
	}

	@Override
	public Object visitContextualProperty(ContextualProperty astNode) {
		// TODO Auto-generated method stub
		return super.visitContextualProperty(astNode);
	}
	@Override
	public Object visitLogExp(LogExp astNode) {

		org.eclipse.qvto.examples.pivot.imperativeocl.LogExp pivotElement = 
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createLogExp());

		pivotElement.getOwnedArguments().addAll(doProcessAll(OCLExpression.class, astNode.getArgument()));

		return pivotElement;
	}
	@Override
	public Object visitContinueExp(ContinueExp astNode) {
		org.eclipse.qvto.examples.pivot.imperativeocl.ContinueExp pivotElement =
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createContinueExp());
		
		//...
		return pivotElement;
	}

	@Override
	public Object visitDictLiteralExp(DictLiteralExp astNode) {
		// TODO Auto-generated method stub
		return super.visitDictLiteralExp(astNode);
	}

	@Override
	public Object visitDictLiteralPart(DictLiteralPart astNode) {
		// TODO Auto-generated method stub
		return super.visitDictLiteralPart(astNode);
	}

	public Object visitEntryOperation(EntryOperation astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.EntryOperation pivotElement =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createEntryOperation());
		pivotElement.setName(astNode.getName());
		pivotElement.getOwnedParameters().addAll(doProcessAll(org.eclipse.ocl.pivot.Parameter.class, astNode.getEParameters()));
		//		pivotOperationalTransformation.setEntry(pivotElement);
		pivotElement.setBody(doProcess(org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody.class, astNode.getBody()));
		//		pivotEntryOperation.setType(createPivotType(traditionalEntryOperation.getEType()));
		return pivotElement;
	}

	@Override
	public Object visitForExp(ForExp astNode) {
		org.eclipse.qvto.examples.pivot.imperativeocl.ForExp pivotElement =
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createForExp());
		pivotElement.getOwnedIterators().addAll(doProcessAll(org.eclipse.ocl.pivot.Variable.class, astNode.getIterator()));
		pivotElement.setOwnedBody(doProcess(OCLExpression.class, astNode.getBody()));
		pivotElement.setOwnedSource(doProcess(OCLExpression.class, astNode.getSource()));
		return pivotElement;
	}

	public Object visitHelper(Helper astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.Helper pivotElement =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createHelper());
		pivotElement.setName(astNode.getName());
		pivotElement.setContext(doProcess(org.eclipse.qvto.examples.pivot.qvtoperational.VarParameter.class, astNode.getContext()));
		pivotElement.getOwnedParameters().addAll(doProcessAll(org.eclipse.ocl.pivot.Parameter.class, astNode.getEParameters()));
		pivotElement.setBody(doProcess(org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody.class, astNode.getBody()));
		pivotElement.setIsQuery(astNode.isIsQuery());
		// astNode.getEType())); // FIXME Bug 479445
		return pivotElement;
	}

	@Override
	public Object visitImperativeIterateExp(ImperativeIterateExp astNode) {

		// TODO Auto-generated method stub
		org.eclipse.qvto.examples.pivot.imperativeocl.ImperativeIterateExp pivotElement = 
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createImperativeIterateExp());
		pivotElement.setOwnedBody(doProcess(org.eclipse.ocl.pivot.OCLExpression.class,astNode.getBody()));
		pivotElement.setTarget(doProcess(org.eclipse.ocl.pivot.Variable.class, astNode.getTarget()));
		pivotElement.setOwnedSource(doProcess(OCLExpression.class, astNode.getSource()));
		pivotElement.getOwnedIterators().addAll(doProcessAll(org.eclipse.ocl.pivot.Variable.class, astNode.getIterator()));
		return pivotElement;
	}

	@Override
	public Object visitImperativeOperation(ImperativeOperation astNode) {
		// TODO Auto-generated method stub
		return super.visitImperativeOperation(astNode);
	}

	@Override
	public Object visitInstantiationExp(InstantiationExp astNode) {
		// TODO Auto-generated method stub
		return super.visitInstantiationExp(astNode);
	}

	@Override
	public Object visitLibrary(Library astNode) {
		// TODO Auto-generated method stub
		return super.visitLibrary(astNode);
	}

	public Object visitMappingBody(MappingBody mappingBody) {
		org.eclipse.qvto.examples.pivot.qvtoperational.MappingBody pivotElement =
				converter.addCreated(mappingBody, QVTOperationalFactory.eINSTANCE.createMappingBody());
		pivotElement.getContent().addAll(doProcessAll(org.eclipse.ocl.pivot.OCLExpression.class, mappingBody.getContent()));
		pivotElement.getInitSection().addAll(doProcessAll(org.eclipse.ocl.pivot.OCLExpression.class, mappingBody.getInitSection()));
		return pivotElement;
	}

	@Override
	public Object visitMappingCallExp(MappingCallExp astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.MappingCallExp pivotElement =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createMappingCallExp());

		return pivotElement;
	}

	public Object visitMappingOperation(MappingOperation astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.MappingOperation pivotElement =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createMappingOperation());
		pivotElement.setName(astNode.getName());
		pivotElement.setContext(doProcess(org.eclipse.qvto.examples.pivot.qvtoperational.VarParameter.class, astNode.getContext()));
		pivotElement.getOwnedParameters().addAll(doProcessAll(org.eclipse.ocl.pivot.Parameter.class, astNode.getEParameters()));
		pivotElement.getResult().addAll(doProcessAll(org.eclipse.qvto.examples.pivot.qvtoperational.VarParameter.class, astNode.getResult()));
		pivotElement.setBody(doProcess(org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody.class, astNode.getBody()));
		List<org.eclipse.ocl.ecore.OCLExpression> whens = astNode.getWhen();
		assert whens.size() <= 1 : "Too many whens in " + astNode;
		if (whens.size() > 0) {
			pivotElement.setWhen(doProcess(org.eclipse.ocl.pivot.OCLExpression.class, whens.get(0)));
		}
		pivotElement.setWhere(doProcess(org.eclipse.ocl.pivot.OCLExpression.class, astNode.getWhere()));
		return pivotElement;
	}

	@Override
	public Object visitModelType(ModelType astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ModelType pivotElement =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createModelType());
		pivotElement.setName(astNode.getName());
		return pivotElement;
	}

	public Object visitModule(Module astNode) {
		if (astNode instanceof OperationalTransformation) {
			return visitOperationalTransformation((OperationalTransformation)astNode);
		}
		org.eclipse.qvto.examples.pivot.qvtoperational.Module pivotElement =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createModule());
		pivotElement.getOwnedOperations().addAll(doProcessAll(org.eclipse.ocl.pivot.Operation.class, astNode.getEOperations()));
		return pivotElement;
	}

	@Override
	public Object visitModuleImport(ModuleImport astNode) {
		// TODO Auto-generated method stub
		return super.visitModuleImport(astNode);
	}

	public Object visitObjectExp(ObjectExp astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ObjectExp pivotElement =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createObjectExp());
		pivotElement.setBody(doProcess(org.eclipse.qvto.examples.pivot.qvtoperational.ConstructorBody.class, astNode.getBody()));
		return pivotElement;
	}

	public Object visitOperationBody(OperationBody astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody pivotElement =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createOperationBody());
		pivotElement.getContent().addAll(doProcessAll(org.eclipse.ocl.pivot.OCLExpression.class, astNode.getContent()));
		return pivotElement;
	}

	@Override
	public Object visitOperationCallExp(OperationCallExp<EClassifier, EOperation> astNode) {
		if (astNode instanceof ImperativeCallExp) {
			org.eclipse.qvto.examples.pivot.qvtoperational.ImperativeCallExp pivotElement =
					converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createImperativeCallExp());
			pivotElement.setOwnedSource(doProcess(org.eclipse.ocl.pivot.OCLExpression.class, astNode.getSource()));
			pivotElement.getOwnedArguments().addAll(doProcessAll(org.eclipse.ocl.pivot.OCLExpression.class, astNode.getArgument()));
			pivotElement.setIsVirtual(((ImperativeCallExp) astNode).isIsVirtual());
			return pivotElement;
		}
		else {
			return super.visitOperationCallExp(astNode);
		}
	}

	public Object visitOperationalTransformation(OperationalTransformation astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.OperationalTransformation pivotClass =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createOperationalTransformation());
		pivotClass.getOwnedOperations().addAll(doProcessAll(org.eclipse.ocl.pivot.Operation.class, astNode.getEOperations()));
		pivotClass.getOwnedVariable().addAll(doProcessAll(org.eclipse.ocl.pivot.Variable.class, astNode.getOwnedVariable()));
		pivotClass.getModelParameter().addAll(doProcessAll(org.eclipse.qvto.examples.pivot.qvtoperational.ModelParameter.class, astNode.getModelParameter()));
		org.eclipse.ocl.pivot.Package pivotPackage = PivotFactory.eINSTANCE.createPackage();
		pivotPackage.setName("");
		Iterable<EClassifier> filteredEClassifiers = Iterables.filter(astNode.getEClassifiers(), new Predicate<EClassifier>()
		{
			@Override
			public boolean apply(EClassifier eClassifier) {
				if (eClassifier instanceof CollectionType) {
					return false;
				}
				if (eClassifier instanceof TupleType) {
					return false;
				}
				if (eClassifier instanceof TypeType) {
					return false;
				}
				return true;
			}
		});
		pivotPackage.getOwnedClasses().addAll(doProcessAll(org.eclipse.ocl.pivot.Class.class, filteredEClassifiers));
		//FIXME intermediate not handled.
		//	pivotPackage.getOwnedPackages().addAll(doProcessAll(org.eclipse.ocl.pivot.Package.class, astNode.getESubpackages()));
		pivotPackage.getOwnedClasses().add(0, pivotClass);
		return pivotPackage;
	}

	@Override
	public Object visitOrderedTupleLiteralExp(OrderedTupleLiteralExp astNode) {
		// TODO Auto-generated method stub
		return super.visitOrderedTupleLiteralExp(astNode);
	}

	@Override
	public Object visitOrderedTupleLiteralPart(OrderedTupleLiteralPart astNode) {
		// TODO Auto-generated method stub
		return super.visitOrderedTupleLiteralPart(astNode);
	}

	@Override
	public Object visitRaiseExp(RaiseExp astNode) {
		// TODO Auto-generated method stub
		return super.visitRaiseExp(astNode);
	}

	@Override
	public Object visitResolveExp(ResolveExp astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ResolveExp pivotElement =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createResolveExp());
		//...
		return pivotElement;
	}

	@Override
	public Object visitResolveInExp(ResolveInExp astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ResolveInExp pivotElement =
				converter.addCreated(astNode, QVTOperationalFactory.eINSTANCE.createResolveInExp());
		//...
		return pivotElement;
	}

	@Override
	public Object visitReturnExp(ReturnExp astNode) {
		org.eclipse.qvto.examples.pivot.imperativeocl.ReturnExp pivotElement =
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createReturnExp());
		pivotElement.setValue(doProcess(OCLExpression.class, astNode.getValue()));
		//.
		return pivotElement;
	}

	@Override
	public Object visitSwitchExp(SwitchExp astNode) {
		// TODO Auto-generated method stub
		org.eclipse.qvto.examples.pivot.imperativeocl.SwitchExp pivotElement = 
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createSwitchExp());

		pivotElement.setElsePart(doProcess(OCLExpression.class, astNode.getElsePart()));
		pivotElement.getAlternativePart().addAll(doProcessAll(org.eclipse.qvto.examples.pivot.imperativeocl.AltExp.class, astNode.getAlternativePart()));
		return pivotElement;
	}

	@Override
	public Object visitTryExp(TryExp astNode) {
		// TODO Auto-generated method stub
		org.eclipse.qvto.examples.pivot.imperativeocl.TryExp pivotElement = 
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createTryExp());
		pivotElement.getTryBody().addAll(doProcessAll(OCLExpression.class, astNode.getTryBody()));
		pivotElement.getExceptClause().addAll(doProcessAll(org.eclipse.qvto.examples.pivot.imperativeocl.CatchExp.class, astNode.getExceptClause()));
		return pivotElement;
	}

	@Override
	public Object visitUnlinkExp(UnlinkExp astNode) {
		// TODO Auto-generated method stub
		return super.visitUnlinkExp(astNode);
	}

	@Override
	public Object visitUnpackExp(UnpackExp astNode) {
		// TODO Auto-generated method stub
		return super.visitUnpackExp(astNode);
	}

	@Override
	public Object visitVarParameter(VarParameter astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.VarParameter pivotElement;
		if (astNode instanceof MappingParameter) {
			pivotElement = QVTOperationalFactory.eINSTANCE.createMappingParameter();
		}
		else if (astNode instanceof ModelParameter) {
			pivotElement = QVTOperationalFactory.eINSTANCE.createModelParameter();
		}
		else {
			pivotElement = QVTOperationalFactory.eINSTANCE.createVarParameter();
		}
		converter.addCreated(astNode, pivotElement);
		pivotElement.setName(astNode.getName());		
		return pivotElement;
	}

	@Override
	public Object visitVariableInitExp(VariableInitExp astNode) {

		org.eclipse.qvto.examples.pivot.imperativeocl.VariableInitExp pivotElement = 
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createVariableInitExp());
		return pivotElement;
	}


	@Override
	public Object visitWhileExp(WhileExp astNode) {
		// TODO Auto-generated method stub

		org.eclipse.qvto.examples.pivot.imperativeocl.WhileExp pivotElement = 
				converter.addCreated(astNode, ImperativeOCLFactory.eINSTANCE.createWhileExp());
		pivotElement.setBody(doProcess(OCLExpression.class, astNode.getBody()));
		pivotElement.setCondition(doProcess(OCLExpression.class, astNode.getCondition()));
		return pivotElement;
	}
}
