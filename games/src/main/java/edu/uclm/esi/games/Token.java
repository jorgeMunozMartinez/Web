package edu.uclm.esi.games;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.bson.BsonDocument;
import org.bson.BsonString;

import edu.uclm.esi.mongolabels.dao.MongoBroker;
import edu.uclm.esi.mongolabels.labels.Bsonable;

public class Token {
	@Bsonable
	private String email;
	@Bsonable
	private String valor;

	public Token createToken(String email) {
		Token token = new Token();
		try {
			token = token.tokenEmail(email);
			token.borrarToken(token.getEmail(), token.getValor());
			createToken(email);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			SecureRandom random = new SecureRandom();
			String text = new BigInteger(130, random).toString(32);
			token.setEmail(email);
			token.setValor(text);
			token.insertToken(token);
			token.sendToken(token);
		}
		return token;
	}

	public Token tokenEmail(String email) throws Exception {
		BsonDocument criterion = new BsonDocument();
		criterion.append("email", new BsonString(email));
		Token token = (Token) MongoBroker.get().loadOne(Token.class, criterion);
		return token;
	}

	public void borrarToken(String email, String tokenValor) {
		BsonDocument criterion = new BsonDocument();
		criterion.append("email", new BsonString(email));
		criterion.append("valor", new BsonString(tokenValor));
		MongoBroker.get().delete("Token", criterion);
	}

	public void sendToken(Token token) {
		EMailSenderService correo = new EMailSenderService();
		correo.enviarPorGmail(token.getEmail(), token.getValor());
	}

	public void insertToken(Token token) {
		try {
			MongoBroker.get().insert(token);
		} catch (Exception e) {
			System.out.println("ERROR MONGO");
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "Token [email=" + email + ", valor=" + valor + "]";
	}
}
