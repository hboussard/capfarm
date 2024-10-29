// Generated from Location.g4 by ANTLR 4.4
package fr.inrae.act.bagap.capfarm.model.constraint;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LocationLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__24=1, T__23=2, T__22=3, T__21=4, T__20=5, T__19=6, T__18=7, T__17=8, 
		T__16=9, T__15=10, T__14=11, T__13=12, T__12=13, T__11=14, T__10=15, T__9=16, 
		T__8=17, T__7=18, T__6=19, T__5=20, T__4=21, T__3=22, T__2=23, T__1=24, 
		T__0=25, INTEGER=26, TEXT=27, WS=28;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'", "'\\u0018'", 
		"'\\u0019'", "'\\u001A'", "'\\u001B'", "'\\u001C'"
	};
	public static final String[] ruleNames = {
		"T__24", "T__23", "T__22", "T__21", "T__20", "T__19", "T__18", "T__17", 
		"T__16", "T__15", "T__14", "T__13", "T__12", "T__11", "T__10", "T__9", 
		"T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", 
		"INTEGER", "TEXT", "WS"
	};


	public LocationLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Location.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\36\u0099\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\2\3\2\3\3\3\3"+
		"\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3"+
		"\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16"+
		"\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24"+
		"\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\33\6\33\u008a\n\33\r\33"+
		"\16\33\u008b\3\34\6\34\u008f\n\34\r\34\16\34\u0090\3\35\6\35\u0094\n\35"+
		"\r\35\16\35\u0095\3\35\3\35\2\2\36\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n"+
		"\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36\3\2\4\6\2\62;C\\aac|\5\2\13\f\17\17\""+
		"\"\u009b\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2"+
		"\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2"+
		"\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2"+
		"\2\2\3;\3\2\2\2\5?\3\2\2\2\7C\3\2\2\2\tE\3\2\2\2\13J\3\2\2\2\rN\3\2\2"+
		"\2\17P\3\2\2\2\21S\3\2\2\2\23W\3\2\2\2\25[\3\2\2\2\27]\3\2\2\2\31_\3\2"+
		"\2\2\33a\3\2\2\2\35e\3\2\2\2\37h\3\2\2\2!j\3\2\2\2#l\3\2\2\2%n\3\2\2\2"+
		"\'p\3\2\2\2)s\3\2\2\2+u\3\2\2\2-w\3\2\2\2/{\3\2\2\2\61\u0084\3\2\2\2\63"+
		"\u0086\3\2\2\2\65\u0089\3\2\2\2\67\u008e\3\2\2\29\u0093\3\2\2\2;<\7)\2"+
		"\2<=\7H\2\2=>\7)\2\2>\4\3\2\2\2?@\7Z\2\2@A\7Q\2\2AB\7T\2\2B\6\3\2\2\2"+
		"CD\7V\2\2D\b\3\2\2\2EF\7C\2\2FG\7T\2\2GH\7G\2\2HI\7C\2\2I\n\3\2\2\2JK"+
		"\7C\2\2KL\7P\2\2LM\7F\2\2M\f\3\2\2\2NO\7?\2\2O\16\3\2\2\2PQ\7>\2\2QR\7"+
		"?\2\2R\20\3\2\2\2ST\7C\2\2TU\7n\2\2UV\7n\2\2V\22\3\2\2\2WX\7C\2\2XY\7"+
		"N\2\2YZ\7N\2\2Z\24\3\2\2\2[\\\7*\2\2\\\26\3\2\2\2]^\7.\2\2^\30\3\2\2\2"+
		"_`\7\60\2\2`\32\3\2\2\2ab\7c\2\2bc\7n\2\2cd\7n\2\2d\34\3\2\2\2ef\7@\2"+
		"\2fg\7?\2\2g\36\3\2\2\2hi\7]\2\2i \3\2\2\2jk\7>\2\2k\"\3\2\2\2lm\7_\2"+
		"\2m$\3\2\2\2no\7@\2\2o&\3\2\2\2pq\7Q\2\2qr\7T\2\2r(\3\2\2\2st\7H\2\2t"+
		"*\3\2\2\2uv\7+\2\2v,\3\2\2\2wx\7)\2\2xy\7V\2\2yz\7)\2\2z.\3\2\2\2{|\7"+
		"F\2\2|}\7K\2\2}~\7U\2\2~\177\7V\2\2\177\u0080\7C\2\2\u0080\u0081\7P\2"+
		"\2\u0081\u0082\7E\2\2\u0082\u0083\7G\2\2\u0083\60\3\2\2\2\u0084\u0085"+
		"\7-\2\2\u0085\62\3\2\2\2\u0086\u0087\7/\2\2\u0087\64\3\2\2\2\u0088\u008a"+
		"\4\62;\2\u0089\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u0089\3\2\2\2\u008b"+
		"\u008c\3\2\2\2\u008c\66\3\2\2\2\u008d\u008f\t\2\2\2\u008e\u008d\3\2\2"+
		"\2\u008f\u0090\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u00918"+
		"\3\2\2\2\u0092\u0094\t\3\2\2\u0093\u0092\3\2\2\2\u0094\u0095\3\2\2\2\u0095"+
		"\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0098\b\35"+
		"\2\2\u0098:\3\2\2\2\6\2\u008b\u0090\u0095\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}