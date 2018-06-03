package in.cyberwalker.alliance.ui.adapters;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.cyberwalker.alliance.BaseActivity;
import in.cyberwalker.alliance.Defaults;
import in.cyberwalker.alliance.R;
import in.cyberwalker.alliance.data.entity.User;
import in.cyberwalker.alliance.ui.EditPeopleActivity;
import in.cyberwalker.alliance.ui.HomeActivity;
import in.cyberwalker.alliance.util.DateUtils;
import in.cyberwalker.alliance.util.PermissionHelper;
import in.cyberwalker.alliance.util.StringUtils;
import in.cyberwalker.alliance.view.QuickContactHelper;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private final BaseActivity context;
    private List<User> users = new ArrayList<>();

    public HomeAdapter(BaseActivity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(View.inflate(parent.getContext(), R.layout.item_user, null));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        User user = users.get(position);
        holder.txvLastContact.setText("Last contacted " + DateUtils.format(user.lastContactedDate, DateUtils.MONTH_DAY_FORMAT));
        int days = DateUtils.daysBetween(user.reminderDate, new Date());
        String append = " Days left";
        int txtColor = holder.txvRemain.getCurrentTextColor();
        if (days == 0) {
            txtColor = context.getResources().getColor(R.color.colorAccent);
            append = " Days left Call today!";
        } else if (days < 0) {
            txtColor = context.getResources().getColor(R.color.colorAccent);
            append = " Day(s) Overdue";
        } else if (days == 1) {
            append = " Day left";
        }
        holder.txvRemain.setText(Math.abs(days) + append);
        holder.txvRemain.setTextColor(txtColor);
        holder.txvName.setText(user.name);

        if (user.isContactImg) {
            new QuickContactHelper(context, holder.circleImageView, user.phoneNumber).addThumbnail();
        } else {
            if (!StringUtils.isNull(user.imgPath)) {
                Bitmap bmp = processAvatarImg(Uri.parse(user.imgPath), context.getContentResolver());
                if (bmp != null) {
                    holder.circleImageView.setImageBitmap(bmp);
                }
            } else {
                holder.circleImageView.setImageResource(R.drawable.ic_person);
            }
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void addUsers(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        public final TextView txvName, txvRemain, txvLastContact;
        public final CircleImageView circleImageView;

        public HomeViewHolder(View itemView) {
            super(itemView);
            txvLastContact = itemView.findViewById(R.id.txvLastContacted);
            txvRemain = itemView.findViewById(R.id.txvRemain);
            txvName = itemView.findViewById(R.id.txvName);
            circleImageView = itemView.findViewById(R.id.imvIcon);
            itemView.setOnClickListener(v -> context.startActivityForResult(new Intent(context, EditPeopleActivity.class)
                    .putExtra("uId", users.get(getAdapterPosition()).id), HomeActivity.REQ_ADD_USER));
        }
    }

    private Bitmap processAvatarImg(Uri data, ContentResolver contentResolver) {
        try {
            if (PermissionHelper.hasPermission(context, Manifest.permission.READ_CONTACTS)) {
                InputStream is = contentResolver.openInputStream(data);
                if (is != null) {
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    return Bitmap.createScaledBitmap(bmp, Defaults.THUMB, Defaults.THUMB, false);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
