package br.com.br.ingresso.validation;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

import br.com.caelum.ingresso.model.Filme;
import br.com.caelum.ingresso.model.Sala;
import br.com.caelum.ingresso.model.Sessao;
import br.com.caelum.ingresso.model.Ingresso;
import br.com.caelum.ingresso.model.desconto.SemDesconto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DescontoTest {

	private Filme filmetest;
	private Sala salatest;
	private Sessao sessaotest;
	private Ingresso ingressotest;

	@Before
	public void preparaDesconto() {

		this.salatest = new Sala("Eldorado	-	IMAX", new BigDecimal("20.5"));
		this.filmetest = new Filme("Rogue	One", Duration.ofMinutes(120), "SCI-FI", new BigDecimal("12"));
		this.sessaotest = new Sessao(LocalTime.parse("10:00:00"), salatest, filmetest);
		this.ingressotest = new Ingresso(sessaotest, new SemDesconto());
	}

	@Test
	public void naoDeveConcederDescontoParaIngressoNormal() {

		BigDecimal precoEsperado = new BigDecimal("32.50");
		Assert.assertEquals(precoEsperado, ingressotest.getPreco());
	}

	@Test
	public void deveConcenderDescontode50PorcentoParaIngressoDeEstudante() {
		 
		BigDecimal precoEspearado = new BigDecimal("16.25");
	 
		Assert.assertEquals(precoEspearado, ingressotest.getPreco());
	}

}
