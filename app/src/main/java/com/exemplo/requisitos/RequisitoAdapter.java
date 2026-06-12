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
import java.util.Locale;

public class RequisitoAdapter extends RecyclerView.Adapter<RequisitoAdapter.ViewHolder> {

    public interface OnRequisitoEditListener   { void onEdit(Requisito requisito); }
    public interface OnRequisitoDeleteListener { void onDelete(Requisito requisito); }

    private final Context context;
    private List<Requisito> lista;
    private final OnRequisitoEditListener   editListener;
    private final OnRequisitoDeleteListener deleteListener;

    public RequisitoAdapter(Context context, List<Requisito> lista,
                            OnRequisitoEditListener editListener,
                            OnRequisitoDeleteListener deleteListener) {
        this.context        = context;
        this.lista          = lista;
        this.editListener   = editListener;
        this.deleteListener = deleteListener;
    }

    public void setLista(List<Requisito> lista) { this.lista = lista; notifyDataSetChanged(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_requisito, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Requisito r = lista.get(position);
        h.tvDescricao.setText(r.getDescricao());
        h.tvData.setText("Registrado: " + r.getDataRegistro());
        h.tvImportancia.setText("Importancia: " + r.getNivelImportancia());
        h.tvDificuldade.setText("Dificuldade: " + r.getNivelDificuldade());
        h.tvTempo.setText(String.format(Locale.getDefault(), "Tempo: %.1fh", r.getTempoEstimado()));
        if (r.getLatitude() != 0 || r.getLongitude() != 0)
            h.tvGps.setText(String.format(Locale.getDefault(), "GPS: %.4f, %.4f", r.getLatitude(), r.getLongitude()));
        else
            h.tvGps.setText("GPS: nao capturado");
        h.btnEditar.setOnClickListener(v -> editListener.onEdit(r));
        h.btnExcluir.setOnClickListener(v -> deleteListener.onDelete(r));
    }

    @Override
    public int getItemCount() { return lista == null ? 0 : lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescricao, tvData, tvImportancia, tvDificuldade, tvTempo, tvGps;
        ImageButton btnEditar, btnExcluir;
        ViewHolder(@NonNull View v) {
            super(v);
            tvDescricao   = v.findViewById(R.id.tvDescricao);
            tvData        = v.findViewById(R.id.tvData);
            tvImportancia = v.findViewById(R.id.tvImportancia);
            tvDificuldade = v.findViewById(R.id.tvDificuldade);
            tvTempo       = v.findViewById(R.id.tvTempo);
            tvGps         = v.findViewById(R.id.tvGps);
            btnEditar     = v.findViewById(R.id.btnEditar);
            btnExcluir    = v.findViewById(R.id.btnExcluir);
        }
    }
}
