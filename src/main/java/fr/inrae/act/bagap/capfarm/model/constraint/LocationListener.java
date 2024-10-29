// Generated from Location.g4 by ANTLR 4.4
package fr.inrae.act.bagap.capfarm.model.constraint;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LocationParser}.
 */
public interface LocationListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LocationParser#area}.
	 * @param ctx the parse tree
	 */
	void enterArea(@NotNull LocationParser.AreaContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#area}.
	 * @param ctx the parse tree
	 */
	void exitArea(@NotNull LocationParser.AreaContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#plusminus}.
	 * @param ctx the parse tree
	 */
	void enterPlusminus(@NotNull LocationParser.PlusminusContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#plusminus}.
	 * @param ctx the parse tree
	 */
	void exitPlusminus(@NotNull LocationParser.PlusminusContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#numatt}.
	 * @param ctx the parse tree
	 */
	void enterNumatt(@NotNull LocationParser.NumattContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#numatt}.
	 * @param ctx the parse tree
	 */
	void exitNumatt(@NotNull LocationParser.NumattContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#distance}.
	 * @param ctx the parse tree
	 */
	void enterDistance(@NotNull LocationParser.DistanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#distance}.
	 * @param ctx the parse tree
	 */
	void exitDistance(@NotNull LocationParser.DistanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#xorterme}.
	 * @param ctx the parse tree
	 */
	void enterXorterme(@NotNull LocationParser.XortermeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#xorterme}.
	 * @param ctx the parse tree
	 */
	void exitXorterme(@NotNull LocationParser.XortermeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#localisation}.
	 * @param ctx the parse tree
	 */
	void enterLocalisation(@NotNull LocationParser.LocalisationContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#localisation}.
	 * @param ctx the parse tree
	 */
	void exitLocalisation(@NotNull LocationParser.LocalisationContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#stringatt}.
	 * @param ctx the parse tree
	 */
	void enterStringatt(@NotNull LocationParser.StringattContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#stringatt}.
	 * @param ctx the parse tree
	 */
	void exitStringatt(@NotNull LocationParser.StringattContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#partout}.
	 * @param ctx the parse tree
	 */
	void enterPartout(@NotNull LocationParser.PartoutContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#partout}.
	 * @param ctx the parse tree
	 */
	void exitPartout(@NotNull LocationParser.PartoutContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#terme}.
	 * @param ctx the parse tree
	 */
	void enterTerme(@NotNull LocationParser.TermeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#terme}.
	 * @param ctx the parse tree
	 */
	void exitTerme(@NotNull LocationParser.TermeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#parcelles}.
	 * @param ctx the parse tree
	 */
	void enterParcelles(@NotNull LocationParser.ParcellesContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#parcelles}.
	 * @param ctx the parse tree
	 */
	void exitParcelles(@NotNull LocationParser.ParcellesContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#boolatt}.
	 * @param ctx the parse tree
	 */
	void enterBoolatt(@NotNull LocationParser.BoolattContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#boolatt}.
	 * @param ctx the parse tree
	 */
	void exitBoolatt(@NotNull LocationParser.BoolattContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#andterme}.
	 * @param ctx the parse tree
	 */
	void enterAndterme(@NotNull LocationParser.AndtermeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#andterme}.
	 * @param ctx the parse tree
	 */
	void exitAndterme(@NotNull LocationParser.AndtermeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#orterme}.
	 * @param ctx the parse tree
	 */
	void enterOrterme(@NotNull LocationParser.OrtermeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#orterme}.
	 * @param ctx the parse tree
	 */
	void exitOrterme(@NotNull LocationParser.OrtermeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LocationParser#evaluate}.
	 * @param ctx the parse tree
	 */
	void enterEvaluate(@NotNull LocationParser.EvaluateContext ctx);
	/**
	 * Exit a parse tree produced by {@link LocationParser#evaluate}.
	 * @param ctx the parse tree
	 */
	void exitEvaluate(@NotNull LocationParser.EvaluateContext ctx);
}