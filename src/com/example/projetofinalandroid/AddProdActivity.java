package com.example.projetofinalandroid;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddProdActivity extends Activity implements OnClickListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProdutoDAO produtoDAO;
	
	private EditText edNome, edQtd, edValorUnico, edValorTotal;
	private Button btAddProd;
	private Button btnCancelar;
	Produto prod = new Produto();
	private ImageView imgProduto;
	private Bitmap bitmap;
	private long id = -1;
	byte[] buffer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_prod);

		produtoDAO = new ProdutoDAO(this);
		
		imgProduto = (ImageView) findViewById(R.id.imgProduto);
		imgProduto.setOnClickListener(this);
		
		btAddProd = (Button) findViewById(R.id.BtnAdd);
		btAddProd.setOnClickListener(this);
		
		btnCancelar = (Button) findViewById(R.id.BtnCancel);
		btnCancelar.setOnClickListener(this);
		
		edNome = (EditText) findViewById(R.id.editTextNome);
		edQtd = (EditText) findViewById(R.id.editTextQtd);
		edValorUnico = (EditText) findViewById(R.id.editTextPrecoUnico);
		btAddProd = (Button) findViewById(R.id.BtnAdd);
		btnCancelar = (Button) findViewById(R.id.BtnCancel);

		Produto p = (Produto) getIntent().getSerializableExtra("produto");
		if (p != null) {
			edNome.setText(p.getNome());
			String preco = String.format("%.2f", p.getPrecoUnitario());
			edValorUnico.setText(preco);
			String quantidade = String.valueOf(p.getQuantidade());
			edQtd.setText(quantidade);
			if (p.getBufferImg() != null)
				imgProduto.setImageBitmap(BitmapFactory.decodeByteArray(p.getBufferImg(), 0, p.getBufferImg().length));
			id = p.getId();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_prod, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		Intent it;
		switch (v.getId()) {
		case R.id.BtnAdd:
			if (camposPreenchidos()) {
				it = new Intent(AddProdActivity.this, ListProdActivity.class);
				
				//Recuperando informações digitadas na tela
				prod.setNome(edNome.getText().toString());
				double preco = Double.parseDouble(edValorUnico.getText().toString().replace(",", "."));
				prod.setPrecoUnitario(preco);
				int quantidade = Integer.parseInt(edQtd.getText().toString());
				prod.setQuantidade(quantidade);				
				
				if (bitmap != null) {
					prod.setBufferImg(buffer);
				}
				
				//Setando o id que veio no objeto de outra Activity para edição
				if (id != -1) {
					prod.setId(id);
				}
				
				it.putExtra("produto", prod);
				
				setResult(RESULT_OK, it);
				finish();
			}
			break;

		case R.id.BtnCancel:
			it = new Intent(AddProdActivity.this, ListProdActivity.class);
			setResult(RESULT_CANCELED, it);
			finish();
			break;
			
		case R.id.imgProduto:
			it = new Intent();
			
			//Localiza imagem do Celular
			it.setType("image/*");
			it.setAction(Intent.ACTION_GET_CONTENT);
			it.addCategory(Intent.CATEGORY_OPENABLE);
			
			startActivityForResult(it, 2);
			break;
		}
		
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		InputStream stream = null;
		if (requestCode == 2 && resultCode == RESULT_OK) {
			if (bitmap != null) {
				bitmap.recycle();
			}
			try {
				stream = getContentResolver().openInputStream(data.getData());
				bitmap = BitmapFactory.decodeStream(stream);
				
				imgProduto.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void excluirProduto(View v) {
		
		Intent it = getIntent();

		final int id = it.getIntExtra("id", 0);

		android.app.AlertDialog.Builder msg = new android.app.AlertDialog.Builder(AddProdActivity.this);

		msg.setMessage("Deseja realmente deletar o produto?");
		msg.setNegativeButton("Não", null);
		msg.setNegativeButton("Sim", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				produtoDAO.excluir(prod);

			}
		});
		msg.show();
		
	}
	
	public void cancelar(View v){
		Intent it = new Intent(AddProdActivity.this, ListProdActivity.class);
		setResult(RESULT_CANCELED, it);
		finish();
	}
	
//	public void getImgProd(View v){
//		Intent it = new Intent();
//		
//		//Busca imagem do Celular
//		it.setType("image/*");
//		it.setAction(Intent.ACTION_GET_CONTENT);
//		it.addCategory(Intent.CATEGORY_OPENABLE);
//		
//		startActivityForResult(it, 2);
//	}
//	
	
	/**
	 * Valida se os campos foram preenchidos
	 * @return true, false
	 */
	private boolean camposPreenchidos() {
		if("".equals(edNome.getText().toString())) {
			msgError("Nome do Produto");
			return false;
		}
		if("".equals(edValorUnico.getText().toString())) {
			msgError("Preço");
			return false;
		}
		if("".equals(edQtd.getText().toString())) {
			msgError("Quantidade");
			return false;
		}
		if (bitmap != null) {
			buffer = getImageForId(bitmap);
			//Se a imagem for muito grande, a mesma nao poderá ser salva 
			if (buffer.length > 200000) {
				msgError("com uma Imagem menor!");
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Método que notifica o usuário 
	 */
	public void msgError(String campo) {
		Toast.makeText(this, "Favor preencha o campo " + campo, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Método que recebe um Bitmap 
	 * converte para PNG e retorna bytes
	 * @param Bitmap
	 * @return byte[]
	 */
	private byte[] getImageForId(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

		return baos.toByteArray();
	}


}
