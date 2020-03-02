# imageloader
图片加载框架
verifyStoragePermissions(this);
        findViewById(R.id.textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });

    }

    private void showImage() {
        String imageUrl = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1583116280&di=60ddffd7efc4ea634bb699a24854e507&src=http://a4.att.hudong.com/21/09/01200000026352136359091694357.jpg";
        ImageLoader.getDefault(this).displayView(imageUrl, imageView);
    }
    
    
    
    博客地址：https:////www.cnblogs.com/tony-yang-flutter/p/12394761.html
