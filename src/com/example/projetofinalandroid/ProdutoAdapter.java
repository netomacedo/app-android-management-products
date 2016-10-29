package com.example.projetofinalandroid;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProdutoAdapter extends BaseAdapter{
	
	private Context context;
	private List<Produto> list;
	
	
	public ProdutoAdapter (Context context,List<Produto> list){
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		
		
		Produto produto = this.list.get(position);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.itemlistlayout, null);
		
		ImageView img = (ImageView) view.findViewById(R.id.imgItem);
		
		/*Se existir imagem cadastrada, entao ela
		 * será exibida no ListView
		 */
		if (produto.getBufferImg() != null) 
			img.setImageBitmap(BitmapFactory.decodeByteArray(produto.getBufferImg(), 0, 
					produto.getBufferImg().length));
		
		TextView nomeProduto = (TextView) view.findViewById(R.id.txtNome);
		nomeProduto.setText(produto.getNome());
		
		TextView preco = (TextView) view.findViewById(R.id.txtPreco);
		//Tratamento para exibicao em forma de moeda
		String valor = String.format("%.2f", produto.getPrecoUnitario());
		preco.setText("R$ " + valor);
		
		TextView qtd = (TextView) view.findViewById(R.id.txtQtd);
		String quantidade = String.valueOf(produto.getQuantidade());
		qtd.setText("Qtd: " + quantidade);
		
		TextView total = (TextView) view.findViewById(R.id.txtTotal);
		//Valor = qtd * valor
		String valorTotal = String.format("%.2f", produto.getQuantidade() * produto.getPrecoUnitario());
		total.setText("R$ " + valorTotal);
		

		return view;

		
	}
	
	static class HolderView{
		TextView textView;
	}

}

