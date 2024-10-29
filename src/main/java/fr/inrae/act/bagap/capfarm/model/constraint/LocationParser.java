// Generated from Location.g4 by ANTLR 4.4
package fr.inrae.act.bagap.capfarm.model.constraint;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LocationParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__24=1, T__23=2, T__22=3, T__21=4, T__20=5, T__19=6, T__18=7, T__17=8, 
		T__16=9, T__15=10, T__14=11, T__13=12, T__12=13, T__11=14, T__10=15, T__9=16, 
		T__8=17, T__7=18, T__6=19, T__5=20, T__4=21, T__3=22, T__2=23, T__1=24, 
		T__0=25, INTEGER=26, TEXT=27, WS=28;
	public static final String[] tokenNames = {
		"<INVALID>", "''F''", "'XOR'", "'T'", "'AREA'", "'AND'", "'='", "'<='", 
		"'All'", "'ALL'", "'('", "','", "'.'", "'all'", "'>='", "'['", "'<'", 
		"']'", "'>'", "'OR'", "'F'", "')'", "''T''", "'DISTANCE'", "'+'", "'-'", 
		"INTEGER", "TEXT", "WS"
	};
	public static final int
		RULE_evaluate = 0, RULE_localisation = 1, RULE_terme = 2, RULE_plusminus = 3, 
		RULE_parcelles = 4, RULE_boolatt = 5, RULE_stringatt = 6, RULE_numatt = 7, 
		RULE_distance = 8, RULE_area = 9, RULE_partout = 10, RULE_andterme = 11, 
		RULE_orterme = 12, RULE_xorterme = 13;
	public static final String[] ruleNames = {
		"evaluate", "localisation", "terme", "plusminus", "parcelles", "boolatt", 
		"stringatt", "numatt", "distance", "area", "partout", "andterme", "orterme", 
		"xorterme"
	};

	@Override
	public String getGrammarFileName() { return "Location.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LocationParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class EvaluateContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(LocationParser.EOF, 0); }
		public LocalisationContext localisation() {
			return getRuleContext(LocalisationContext.class,0);
		}
		public EvaluateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_evaluate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterEvaluate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitEvaluate(this);
		}
	}

	public final EvaluateContext evaluate() throws RecognitionException {
		EvaluateContext _localctx = new EvaluateContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_evaluate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28); localisation();
			setState(29); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocalisationContext extends ParserRuleContext {
		public List<TermeContext> terme() {
			return getRuleContexts(TermeContext.class);
		}
		public PartoutContext partout() {
			return getRuleContext(PartoutContext.class,0);
		}
		public List<PlusminusContext> plusminus() {
			return getRuleContexts(PlusminusContext.class);
		}
		public PlusminusContext plusminus(int i) {
			return getRuleContext(PlusminusContext.class,i);
		}
		public TermeContext terme(int i) {
			return getRuleContext(TermeContext.class,i);
		}
		public LocalisationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localisation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterLocalisation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitLocalisation(this);
		}
	}

	public final LocalisationContext localisation() throws RecognitionException {
		LocalisationContext _localctx = new LocalisationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_localisation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(36);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(31); partout();
				}
				break;
			case 2:
				{
				setState(33);
				_la = _input.LA(1);
				if (_la==T__1 || _la==T__0) {
					{
					setState(32); plusminus();
					}
				}

				setState(35); terme();
				}
				break;
			}
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1 || _la==T__0) {
				{
				{
				setState(38); plusminus();
				setState(39); terme();
				}
				}
				setState(45);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermeContext extends ParserRuleContext {
		public NumattContext numatt() {
			return getRuleContext(NumattContext.class,0);
		}
		public OrtermeContext orterme() {
			return getRuleContext(OrtermeContext.class,0);
		}
		public DistanceContext distance() {
			return getRuleContext(DistanceContext.class,0);
		}
		public XortermeContext xorterme() {
			return getRuleContext(XortermeContext.class,0);
		}
		public BoolattContext boolatt() {
			return getRuleContext(BoolattContext.class,0);
		}
		public AreaContext area() {
			return getRuleContext(AreaContext.class,0);
		}
		public StringattContext stringatt() {
			return getRuleContext(StringattContext.class,0);
		}
		public AndtermeContext andterme() {
			return getRuleContext(AndtermeContext.class,0);
		}
		public ParcellesContext parcelles() {
			return getRuleContext(ParcellesContext.class,0);
		}
		public TermeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_terme; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterTerme(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitTerme(this);
		}
	}

	public final TermeContext terme() throws RecognitionException {
		TermeContext _localctx = new TermeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_terme);
		try {
			setState(55);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(46); parcelles();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(47); boolatt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(48); stringatt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(49); numatt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(50); distance();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(51); area();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(52); andterme();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(53); orterme();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(54); xorterme();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PlusminusContext extends ParserRuleContext {
		public PlusminusContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plusminus; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterPlusminus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitPlusminus(this);
		}
	}

	public final PlusminusContext plusminus() throws RecognitionException {
		PlusminusContext _localctx = new PlusminusContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_plusminus);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			_la = _input.LA(1);
			if ( !(_la==T__1 || _la==T__0) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParcellesContext extends ParserRuleContext {
		public List<TerminalNode> TEXT() { return getTokens(LocationParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(LocationParser.TEXT, i);
		}
		public ParcellesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parcelles; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterParcelles(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitParcelles(this);
		}
	}

	public final ParcellesContext parcelles() throws RecognitionException {
		ParcellesContext _localctx = new ParcellesContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_parcelles);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59); match(T__10);
			setState(60); match(TEXT);
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(61); match(T__14);
				setState(62); match(TEXT);
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(68); match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BoolattContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(LocationParser.TEXT, 0); }
		public BoolattContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolatt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterBoolatt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitBoolatt(this);
		}
	}

	public final BoolattContext boolatt() throws RecognitionException {
		BoolattContext _localctx = new BoolattContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_boolatt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70); match(T__10);
			setState(71); match(TEXT);
			setState(77);
			_la = _input.LA(1);
			if (_la==T__19) {
				{
				setState(72); match(T__19);
				setState(75);
				switch (_input.LA(1)) {
				case T__22:
				case T__3:
					{
					setState(73);
					_la = _input.LA(1);
					if ( !(_la==T__22 || _la==T__3) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					break;
				case T__24:
				case T__5:
					{
					setState(74);
					_la = _input.LA(1);
					if ( !(_la==T__24 || _la==T__5) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(79); match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringattContext extends ParserRuleContext {
		public List<TerminalNode> TEXT() { return getTokens(LocationParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(LocationParser.TEXT, i);
		}
		public StringattContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringatt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterStringatt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitStringatt(this);
		}
	}

	public final StringattContext stringatt() throws RecognitionException {
		StringattContext _localctx = new StringattContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_stringatt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81); match(T__10);
			setState(82); match(TEXT);
			setState(83); match(T__19);
			setState(84); match(TEXT);
			setState(85); match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumattContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(LocationParser.TEXT, 0); }
		public List<TerminalNode> INTEGER() { return getTokens(LocationParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(LocationParser.INTEGER, i);
		}
		public NumattContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numatt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterNumatt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitNumatt(this);
		}
	}

	public final NumattContext numatt() throws RecognitionException {
		NumattContext _localctx = new NumattContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_numatt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87); match(T__10);
			setState(88); match(TEXT);
			setState(89);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__19) | (1L << T__18) | (1L << T__11) | (1L << T__9) | (1L << T__7))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(90); match(INTEGER);
			setState(93);
			_la = _input.LA(1);
			if (_la==T__13) {
				{
				setState(91); match(T__13);
				setState(92); match(INTEGER);
				}
			}

			setState(95); match(T__8);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DistanceContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(LocationParser.TEXT, 0); }
		public List<TerminalNode> INTEGER() { return getTokens(LocationParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(LocationParser.INTEGER, i);
		}
		public DistanceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_distance; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterDistance(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitDistance(this);
		}
	}

	public final DistanceContext distance() throws RecognitionException {
		DistanceContext _localctx = new DistanceContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_distance);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97); match(T__2);
			setState(98); match(T__15);
			setState(99); match(TEXT);
			setState(100);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__19) | (1L << T__18) | (1L << T__11) | (1L << T__9) | (1L << T__7))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(101); match(INTEGER);
			setState(104);
			_la = _input.LA(1);
			if (_la==T__13) {
				{
				setState(102); match(T__13);
				setState(103); match(INTEGER);
				}
			}

			setState(106); match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AreaContext extends ParserRuleContext {
		public List<TerminalNode> INTEGER() { return getTokens(LocationParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(LocationParser.INTEGER, i);
		}
		public AreaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_area; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterArea(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitArea(this);
		}
	}

	public final AreaContext area() throws RecognitionException {
		AreaContext _localctx = new AreaContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_area);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108); match(T__21);
			setState(109);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__19) | (1L << T__18) | (1L << T__11) | (1L << T__9) | (1L << T__7))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(110); match(INTEGER);
			setState(113);
			_la = _input.LA(1);
			if (_la==T__13) {
				{
				setState(111); match(T__13);
				setState(112); match(INTEGER);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PartoutContext extends ParserRuleContext {
		public PartoutContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partout; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterPartout(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitPartout(this);
		}
	}

	public final PartoutContext partout() throws RecognitionException {
		PartoutContext _localctx = new PartoutContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_partout);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__16) | (1L << T__12))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AndtermeContext extends ParserRuleContext {
		public LocalisationContext localisation(int i) {
			return getRuleContext(LocalisationContext.class,i);
		}
		public List<LocalisationContext> localisation() {
			return getRuleContexts(LocalisationContext.class);
		}
		public AndtermeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andterme; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterAndterme(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitAndterme(this);
		}
	}

	public final AndtermeContext andterme() throws RecognitionException {
		AndtermeContext _localctx = new AndtermeContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_andterme);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117); match(T__20);
			setState(118); match(T__15);
			setState(119); localisation();
			setState(120); match(T__14);
			setState(121); localisation();
			setState(126);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(122); match(T__14);
				setState(123); localisation();
				}
				}
				setState(128);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(129); match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrtermeContext extends ParserRuleContext {
		public LocalisationContext localisation(int i) {
			return getRuleContext(LocalisationContext.class,i);
		}
		public List<LocalisationContext> localisation() {
			return getRuleContexts(LocalisationContext.class);
		}
		public OrtermeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orterme; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterOrterme(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitOrterme(this);
		}
	}

	public final OrtermeContext orterme() throws RecognitionException {
		OrtermeContext _localctx = new OrtermeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_orterme);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131); match(T__6);
			setState(132); match(T__15);
			setState(133); localisation();
			setState(134); match(T__14);
			setState(135); localisation();
			setState(140);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(136); match(T__14);
				setState(137); localisation();
				}
				}
				setState(142);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(143); match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XortermeContext extends ParserRuleContext {
		public LocalisationContext localisation(int i) {
			return getRuleContext(LocalisationContext.class,i);
		}
		public List<LocalisationContext> localisation() {
			return getRuleContexts(LocalisationContext.class);
		}
		public XortermeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xorterme; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterXorterme(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitXorterme(this);
		}
	}

	public final XortermeContext xorterme() throws RecognitionException {
		XortermeContext _localctx = new XortermeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_xorterme);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145); match(T__23);
			setState(146); match(T__15);
			setState(147); localisation();
			setState(148); match(T__14);
			setState(149); localisation();
			setState(154);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(150); match(T__14);
				setState(151); localisation();
				}
				}
				setState(156);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(157); match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\36\u00a2\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\3\3\3\5\3$\n\3"+
		"\3\3\5\3\'\n\3\3\3\3\3\3\3\7\3,\n\3\f\3\16\3/\13\3\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\5\4:\n\4\3\5\3\5\3\6\3\6\3\6\3\6\7\6B\n\6\f\6\16\6E\13"+
		"\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\5\7N\n\7\5\7P\n\7\3\7\3\7\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\5\t`\n\t\3\t\3\t\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\5\nk\n\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\5\13t\n\13\3\f\3"+
		"\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\7\r\177\n\r\f\r\16\r\u0082\13\r\3\r\3\r"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\7\16\u008d\n\16\f\16\16\16\u0090\13"+
		"\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\7\17\u009b\n\17\f\17"+
		"\16\17\u009e\13\17\3\17\3\17\3\17\2\2\20\2\4\6\b\n\f\16\20\22\24\26\30"+
		"\32\34\2\7\3\2\32\33\4\2\5\5\30\30\4\2\3\3\26\26\6\2\b\t\20\20\22\22\24"+
		"\24\4\2\n\13\17\17\u00a8\2\36\3\2\2\2\4&\3\2\2\2\69\3\2\2\2\b;\3\2\2\2"+
		"\n=\3\2\2\2\fH\3\2\2\2\16S\3\2\2\2\20Y\3\2\2\2\22c\3\2\2\2\24n\3\2\2\2"+
		"\26u\3\2\2\2\30w\3\2\2\2\32\u0085\3\2\2\2\34\u0093\3\2\2\2\36\37\5\4\3"+
		"\2\37 \7\2\2\3 \3\3\2\2\2!\'\5\26\f\2\"$\5\b\5\2#\"\3\2\2\2#$\3\2\2\2"+
		"$%\3\2\2\2%\'\5\6\4\2&!\3\2\2\2&#\3\2\2\2&\'\3\2\2\2\'-\3\2\2\2()\5\b"+
		"\5\2)*\5\6\4\2*,\3\2\2\2+(\3\2\2\2,/\3\2\2\2-+\3\2\2\2-.\3\2\2\2.\5\3"+
		"\2\2\2/-\3\2\2\2\60:\5\n\6\2\61:\5\f\7\2\62:\5\16\b\2\63:\5\20\t\2\64"+
		":\5\22\n\2\65:\5\24\13\2\66:\5\30\r\2\67:\5\32\16\28:\5\34\17\29\60\3"+
		"\2\2\29\61\3\2\2\29\62\3\2\2\29\63\3\2\2\29\64\3\2\2\29\65\3\2\2\29\66"+
		"\3\2\2\29\67\3\2\2\298\3\2\2\2:\7\3\2\2\2;<\t\2\2\2<\t\3\2\2\2=>\7\21"+
		"\2\2>C\7\35\2\2?@\7\r\2\2@B\7\35\2\2A?\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3"+
		"\2\2\2DF\3\2\2\2EC\3\2\2\2FG\7\23\2\2G\13\3\2\2\2HI\7\21\2\2IO\7\35\2"+
		"\2JM\7\b\2\2KN\t\3\2\2LN\t\4\2\2MK\3\2\2\2ML\3\2\2\2NP\3\2\2\2OJ\3\2\2"+
		"\2OP\3\2\2\2PQ\3\2\2\2QR\7\23\2\2R\r\3\2\2\2ST\7\21\2\2TU\7\35\2\2UV\7"+
		"\b\2\2VW\7\35\2\2WX\7\23\2\2X\17\3\2\2\2YZ\7\21\2\2Z[\7\35\2\2[\\\t\5"+
		"\2\2\\_\7\34\2\2]^\7\16\2\2^`\7\34\2\2_]\3\2\2\2_`\3\2\2\2`a\3\2\2\2a"+
		"b\7\23\2\2b\21\3\2\2\2cd\7\31\2\2de\7\f\2\2ef\7\35\2\2fg\t\5\2\2gj\7\34"+
		"\2\2hi\7\16\2\2ik\7\34\2\2jh\3\2\2\2jk\3\2\2\2kl\3\2\2\2lm\7\27\2\2m\23"+
		"\3\2\2\2no\7\6\2\2op\t\5\2\2ps\7\34\2\2qr\7\16\2\2rt\7\34\2\2sq\3\2\2"+
		"\2st\3\2\2\2t\25\3\2\2\2uv\t\6\2\2v\27\3\2\2\2wx\7\7\2\2xy\7\f\2\2yz\5"+
		"\4\3\2z{\7\r\2\2{\u0080\5\4\3\2|}\7\r\2\2}\177\5\4\3\2~|\3\2\2\2\177\u0082"+
		"\3\2\2\2\u0080~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\u0083\3\2\2\2\u0082"+
		"\u0080\3\2\2\2\u0083\u0084\7\27\2\2\u0084\31\3\2\2\2\u0085\u0086\7\25"+
		"\2\2\u0086\u0087\7\f\2\2\u0087\u0088\5\4\3\2\u0088\u0089\7\r\2\2\u0089"+
		"\u008e\5\4\3\2\u008a\u008b\7\r\2\2\u008b\u008d\5\4\3\2\u008c\u008a\3\2"+
		"\2\2\u008d\u0090\3\2\2\2\u008e\u008c\3\2\2\2\u008e\u008f\3\2\2\2\u008f"+
		"\u0091\3\2\2\2\u0090\u008e\3\2\2\2\u0091\u0092\7\27\2\2\u0092\33\3\2\2"+
		"\2\u0093\u0094\7\4\2\2\u0094\u0095\7\f\2\2\u0095\u0096\5\4\3\2\u0096\u0097"+
		"\7\r\2\2\u0097\u009c\5\4\3\2\u0098\u0099\7\r\2\2\u0099\u009b\5\4\3\2\u009a"+
		"\u0098\3\2\2\2\u009b\u009e\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2"+
		"\2\2\u009d\u009f\3\2\2\2\u009e\u009c\3\2\2\2\u009f\u00a0\7\27\2\2\u00a0"+
		"\35\3\2\2\2\17#&-9CMO_js\u0080\u008e\u009c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}