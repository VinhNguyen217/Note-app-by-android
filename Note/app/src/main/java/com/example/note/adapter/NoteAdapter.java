package com.example.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.MainActivity;
import com.example.note.R;
import com.example.note.RecyclerViewClickInterface;
import com.example.note.model.Note;

import java.util.ArrayList;
import java.util.Collection;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements Filterable {
    private ArrayList<Note> noteList;               //Danh sách chính
    private ArrayList<Note> noteListAll;            //Danh sách phụ chứa đầy đủ dữ liệu như danh sách chính
    private RecyclerViewClickInterface recyclerViewClickInterface;      //Interface sử dụng để bắt sự kiện khi người dùng nhấp vào 1 mục trên danh sách

    /**
     * Hàm tạo
     *
     * @param noteList
     * @param recyclerViewClickInterface
     */
    public NoteAdapter(ArrayList<Note> noteList, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.noteList = noteList;
        this.noteListAll = new ArrayList<>(noteList);
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    /**
     * Xóa toàn bộ dữ liệu trong danh sách ghi chú
     */
    public void clearData(){
        noteList.clear();
        noteListAll.clear();
    }

    /**
     * Xóa 1 ghi chú theo yêu cầu người dùng
     * @param idList
     */
    public void deleteNote(int idList){
        noteList.remove(idList);
        noteListAll.remove(idList);
    }

    /**
     * Tạo layout hiện thị trên màn hình
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_note, parent, false);
        return new NoteViewHolder(view);
    }

    /**
     * Xét dữ liệu hiển thị
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        if (note == null) {
            return;
        }
        holder.img_note_list.setImageResource(R.drawable.note_list);
        holder.tv_title_list.setText(note.getTitle());
        holder.tv_time_list.setText(note.getTimeCreate());
        holder.layout_note_list.setBackgroundResource(note.getColor());
    }

    /**
     * Số lượng item trong danh sách
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (noteList != null)
            return noteList.size();
        else return 0;
    }

    /**
     * Hàm tìm kiếm trên danh sách
     *
     * @return
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            /**
             * Xử lý logic khi người dùng nhập vào thanh tìm kiếm
             * @param constraint
             * @return
             */
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Note> filteredList = new ArrayList<>();
                String strSearch = constraint.toString();

                //Nếu thanh tìm kiếm rỗng thì trả về toàn bộ danh sách các giá trị ban đầu
                if (strSearch.isEmpty()) {
                    filteredList.addAll(noteListAll);
                }
                //Tìm kiếm theo tiêu đề , chỉ cần ký tự người dùng tìm kiếm nằm trong tiêu đề thì tiêu đề đó sẽ hiện ra
                else {
                    for (Note note : noteListAll) {
                        if (note.getTitle().toLowerCase().contains(strSearch.toLowerCase())) {
                            filteredList.add(note);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            /**
             * Hiện thị kết quả theo từng trạng thái tìm kiếm
             * @param constraint
             * @param results
             */
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                noteList.clear();
                noteList.addAll((Collection<? extends Note>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    /**
     * Tạo NoteViewHolder kế thừa từ ViewHolder trong RecyclerView
     */
    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_note_list;
        private TextView tv_title_list, tv_time_list;
        private ConstraintLayout layout_note_list;

        /**
         * Hàm tạo
         *
         * @param itemView
         */
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            img_note_list = (ImageView) itemView.findViewById(R.id.img_note_list);
            tv_title_list = (TextView) itemView.findViewById(R.id.tv_title_list);
            tv_time_list = (TextView) itemView.findViewById(R.id.tv_time_list);
            layout_note_list = (ConstraintLayout) itemView.findViewById(R.id.layout_note_list);


            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * Băt sự kiện khi người dùng nhấp vào 1 mục trên danh sách
                 * @param v
                 */
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.OnItemClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                /**
                 * Bắt sự kiện khi người dùng giữ lâu 1 mục trên danh sách
                 * @param v
                 * @return
                 */
                @Override
                public boolean onLongClick(View v) {
                    recyclerViewClickInterface.OnItemLongClick(getAdapterPosition());
                    return false;
                }
            });
        }
    }
}
