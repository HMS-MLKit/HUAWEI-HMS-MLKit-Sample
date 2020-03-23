/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mlkit.sample.activity.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.huawei.hms.mlsdk.productvisionsearch.MLVisionSearchProductImage;
import com.mlkit.sample.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductVisionSearchAdapter extends RecyclerView.Adapter<ProductVisionSearchAdapter.ViewHolder> {
    private List<MLVisionSearchProductImage> productImageList;

    public ProductVisionSearchAdapter(List<MLVisionSearchProductImage> productImageList) {
        this.productImageList = productImageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(parent.getContext(), R.layout.activity_product_vision_search_dialog_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MLVisionSearchProductImage productImage = this.productImageList.get(position);

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        String url = getImageUrl(productImage.getProductId(), productImage.getImageId());
        LazyHeaders headers = new LazyHeaders.Builder()
                .addHeader("Accept-Encoding", "identity")
                .build();

        Glide.with(holder.itemView).load(new GlideUrl(url, headers))
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.image);

        String name = String.format("%s(%.5f)", productImage.getProductId(), productImage.getPossibility());
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return this.productImageList == null ? 0 : this.productImageList.size();
    }

    private String getImageUrl(String productId, String imageId) {
        StringBuffer buffer = new StringBuffer("http://117.78.10.127:8083/internal/mlkit/v1/snap/image/download?");
        buffer.append("productId=" + productId);
        buffer.append("&");
        buffer.append("imageId=" + imageId);

        return buffer.toString();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public TextView name;

        public ViewHolder(View view) {
            super(view);

            this.image = view.findViewById(R.id.image);
            this.name = view.findViewById(R.id.name);
        }
    }
}
