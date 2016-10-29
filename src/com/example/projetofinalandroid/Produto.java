package com.example.projetofinalandroid;

import java.io.Serializable;


public class Produto implements Serializable{
	
	private static final long serialVersionUID = 5867544897204646115L;
	
	private long id;
	private String nome;
	private int quantidade;
	private double precoUnitario;
	private byte[] bufferImg;
	
	public Produto(String nome, double precoUnitario, int quantidade, byte[] img) {
		this.nome = nome;
		this.precoUnitario = precoUnitario;
		this.quantidade = quantidade;
		this.bufferImg = img;
	}

	public Produto() {
		// TODO Auto-generated constructor stub
	}

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public byte[] getBufferImg() {
		return bufferImg;
	}

	public void setBufferImg(byte[] bufferImg) {
		this.bufferImg = bufferImg;
	}

	

}
