package br.com.br.ingresso.validation;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.ingresso.model.Filme;
import br.com.caelum.ingresso.model.Sala;
import br.com.caelum.ingresso.model.Sessao;
import br.com.caelum.ingresso.validacao.GerenciadorDeSessao;

public class GerenciadorDeSessaoTest {

	private Filme rogueOne;
	private Sala sala3D;
	private Sessao sessaoDasDez;
	private Sessao sessaoDasTreze;
	private Sessao sessaoDasDezoito;

	@Before
	public void preparaSessoes() {

		this.rogueOne = new Filme("Rogue One", Duration.ofMinutes(120), "SCI-FI", BigDecimal.ZERO);
		this.sala3D = new Sala("Sala3D", BigDecimal.ZERO);

		this.sessaoDasDez = new Sessao(LocalTime.parse("10:00:00"), sala3D, rogueOne);
		this.sessaoDasTreze = new Sessao(LocalTime.parse("13:00:00"), sala3D, rogueOne);
		this.sessaoDasDezoito = new Sessao(LocalTime.parse("18:00:00"), sala3D, rogueOne);
	}

	@Test
	public void deveAdiconarSerListaDaSessaoEstiverVazia() {

		List<Sessao> sessoes = Collections.emptyList();
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoes);

		Filme filme = new Filme("Rogue One", Duration.ofMinutes(120), "SCI-FI", BigDecimal.ZERO);
		filme.setDuracao(120);
		LocalTime horario = LocalTime.parse("10:00:00");
		Sala sala = new Sala("", BigDecimal.ZERO);

		Sessao sessao = new Sessao(horario, sala, filme);

		boolean cabe = gerenciador.cabe(sessao);

		Assert.assertTrue(cabe);
	}

	@Test
	public void garanteQueNaoDevePermitirSessaoNoMesmoHorario() {
		List<Sessao> sessoes = Arrays.asList(sessaoDasDez);
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoes);
		Assert.assertFalse(gerenciador.cabe(sessaoDasDez));
	}

	@Test
	public void garanteQueNaoDevePermitirSessoesTerminandoDentroDoHorarioDeUmaSessaoJaExistente() {
		List<Sessao> sessoes = Arrays.asList(sessaoDasDez);
		Sessao sessao = new Sessao(sessaoDasDez.getHorario().minusHours(1), sala3D, rogueOne);
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoes);
		Assert.assertFalse(gerenciador.cabe(sessao));
	}

	@Test
	public void garanteQueNaoDevePermitirSessoesIniciandoDentroDoHorarioDeUmaSessaoJaExistente() {
		List<Sessao> sessoesDaSala = Arrays.asList(sessaoDasDez);
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoesDaSala);
		Sessao sessao = new Sessao(sessaoDasDez.getHorario().plusHours(1), sala3D, rogueOne);
		Assert.assertFalse(gerenciador.cabe(sessao));
	}

	@Test
	public void garanteQueDevePermitirUmaInsercaoEntreDoisFilmes() {
		List<Sessao> sessoes = Arrays.asList(sessaoDasDez, sessaoDasDezoito);
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoes);
		Assert.assertTrue(gerenciador.cabe(sessaoDasTreze));
	}

	@Test
	public void garanteQueDeveNaoPermitirUmaSessaoQueTerminaNoProximoDia() {
		List<Sessao> sessoes = Collections.emptyList();
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoes);
		Sessao sessaoQueTerminaAmanha = new Sessao(LocalTime.parse("23:00:00"), sala3D, rogueOne);

		Assert.assertFalse(gerenciador.cabe(sessaoQueTerminaAmanha));
	}

	@Test
	public void oPrecoDaSessaoDeveSerIgualASomaDoSalaMaisDoFilme() {

		Sala sala = new Sala("Eldorado	-	IMax", new BigDecimal("22.5"));
		Filme filme = new Filme("Rogue	One", Duration.ofMinutes(120), "SCI-FI", new BigDecimal("12.0"));
		BigDecimal somaDosPrecosDaSalaEFilme = sala.getPreco().add(filme.getPreco());
		Sessao sessao = new Sessao(LocalTime.parse("10:00:00"), sala, filme);
		Assert.assertEquals(somaDosPrecosDaSalaEFilme, sessao.getPreco());
	}

}
