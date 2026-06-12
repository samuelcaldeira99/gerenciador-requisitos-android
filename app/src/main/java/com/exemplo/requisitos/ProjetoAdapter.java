package com.exemplo.requisitos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProjetoAdapter extends RecyclerView.Adapter<ProjetoAdapter.ViewHolder> {

    public interface OnProjetoClickListener  { void onClick(Projeto projeto); }
    public interface OnProjetoEditListener   { void onEdit(Projeto projeto); }
    public interface OnProjetoDeleteListener { void onDelete(Projeto projeto); }
    public interface OnProjetoWebViewListener { void onWebView(Projeto projeto); }

    private final Context context;
    private List<Projeto> lista;
    private final OnProjetoClickListener   clickListener;
    private final OnProjetoEditListener    editListener;
    private final OnProjetoDeleteListener  deleteListener;
    private final OnProjetoWebViewListener webViewListener;

    public ProjetoAdapter(Context context, List<Projeto> lista,
                          OnProjetoClickListener clickListener,
                          OnProjetoEditListener editListener,
                          OnProjetoDeleteListener deleteListener,
                          OnProjetoWebViewListener webViewListener) {
        this.context         = context;
        this.lista           = lista;
        this.clickListener   = clickListener;
        this.editListener    = editListener;
        this.deleteListener  = deleteListener;
        this.webViewListener = webViewListener;
    }

    public void setLista(List<Projeto> lista) { this.lista = lista; notifyDataSetChanged(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_projeto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Projeto p = lista.get(position);
        h.tvNome.setText(p.getNome());
        h.tvDatas.setText("Inicio: " + p.getDataInicio() + "  |  Entrega: " + p.getDataEntrega());

        // Mostrar link se existir
        String link = p.getLinkDocumentacao();
        if (link != null && !link.isEmpty()) {
            h.tvLink.setVisibility(View.VISIBLE);
            h.tvLink.setText("🔗 " + link);
            h.btnWebView.setAlpha(1.0f);
        } else {
            h.tvLink.setVisibility(View.GONE);
            h.btnWebView.setAlpha(0.3f); // botão apagado quando sem link
        }

        h.itemView.setOnClickListener(v -> clickListener.onClick(p));
        h.btnEditar.setOnClickListener(v -> editListener.onEdit(p));
        h.btnExcluir.setOnClickListener(v -> deleteListener.onDelete(p));
        h.btnWebView.setOnClickListener(v -> webViewListener.onWebView(p));
    }

    @Override
    public int getItemCount() { return lista == null ? 0 : lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvDatas, tvLink;
        ImageButton btnEditar, btnExcluir, btnWebView;
        ViewHolder(@NonNull View v) {
            super(v);
            tvNome    = v.findViewById(R.id.tvNome);
            tvDatas   = v.findViewById(R.id.tvDatas);
            tvLink    = v.findViewById(R.id.tvLink);
            btnEditar  = v.findViewById(R.id.btnEditar);
            btnExcluir = v.findViewById(R.id.btnExcluir);
            btnWebView = v.findViewById(R.id.btnWebView);
        }
    }
}
