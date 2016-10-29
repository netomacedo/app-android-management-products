package com.example.projetofinalandroid;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListProdActivity extends Activity implements OnItemClickListener,
OnItemLongClickListener, OnClickListener{
	
	private ProdutoAdapter adapter;
	private ListView listView;
	private List<Produto> produtos;
	private Button btnAdd;
	private TextView txtSubTotal;
	private ProdutoDAO dao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_prod);

		dao = new ProdutoDAO(this);

		listView = (ListView) findViewById(R.id.listProd);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);

		btnAdd = (Button) findViewById(R.id.btAddProd);
		btnAdd.setOnClickListener(this);
		
		txtSubTotal = (TextView) findViewById(R.id.txtSubTotal);

		carregaListaProdutos();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.btAddProd:
			i = new Intent(ListProdActivity.this, AddProdActivity.class);
			startActivityForResult(i, 0);
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		final int posicao = position;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Deseja excluir?");

		// set dialog message
		alertDialogBuilder
				.setMessage("Clique em Sim para excluir!")
				.setCancelable(false)
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Produto produto = produtos.get(posicao);
								produtos.remove(produto);
								dao.excluir(produto);
								carregaListaProdutos();
								setMessage("excluído");
							}
						})
				.setNegativeButton("Não", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Produto produto = produtos.get(position);
		Intent i = new Intent(ListProdActivity.this, AddProdActivity.class);
		
		i.putExtra("produto", produto);
		startActivityForResult(i, 1);

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			
			Produto p = (Produto) data.getSerializableExtra("produto");
			
			
			if (requestCode == 0) {
				//Caso a imagem não seja preenchida, insere a default
				if (p.getBufferImg() == null) {
					p.setBufferImg(getImageForId(R.drawable.no_image_));
				}
				//Adiciona o produto a lista e ao Banco
				produtos.add(p);
				dao.inserir(p);
				
				setMessage("cadastrado");
			}else if (requestCode == 1) {
				//Caso a imagem seja nula, continua a imagem antiga. 
				if (p.getBufferImg() == null) {
					Produto pd = dao.buscarProduto((int) p.getId());
					p.setBufferImg(pd.getBufferImg());
				}
				//Atualiza o produto no banco
				dao.atualizar(p);
				
				setMessage("atualizado");
			}
			carregaListaProdutos();
		}
	}
	
	/**
	 * Método que busca todos os registros no banco, insere em uma lista
	 * e recarrega o ListView com o adapter
	 */
	private void carregaListaProdutos() {
		produtos = new ArrayList<Produto>();
		produtos = dao.buscarTodosProdutos();

		adapter = new ProdutoAdapter(this, produtos);
		listView.setAdapter(adapter);
		
		calculaSubTotal();
	}
	
	/**
	 * Método que calcula e informa o subtotal na tela
	 */
	private void calculaSubTotal() {
		double subTotal = 0;
		for (int i = 0; i < produtos.size(); i++) {
			subTotal += produtos.get(i).getPrecoUnitario() * produtos.get(i).getQuantidade();
		}
		
		txtSubTotal.setText("Subtotal: R$ " + String.format("%.2f", subTotal));
	}

	/**
	 * Método que recebe um ID de um Drawable e retorna bytes
	 * @param id
	 * @return bytes de um drawable
	 */
	private byte[] getImageForId(int id) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

		return baos.toByteArray();
	}
	
	/**
	 * Método que notifica o usuário através de mensagens na tela
	 * @param message
	 */
	private void setMessage(String message) {
		Toast.makeText(this, "Produto " + message + " com sucesso!", Toast.LENGTH_SHORT).show();
	}

}
