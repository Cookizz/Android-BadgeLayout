# Android-BadgeLayout
Offers an easier way to design, create and control badges within your Android apps. You pay no attention to any `View` issues during your development.

## Usage
*For a simple implementation of this project see the `badgedemo/` folder.*

  1. Include `BadgeRelativeLayout` in your view. Generally, it should be a container of those target views who you intend to put badges on.
  
  (no ListView/ScrollView inside the container, they'll be supported later on)
  
        <com.cookizz.badgelib.BadgeRelativeLayout
            android:id="@+id/badge_manager"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
            
            <Button
                android:id="@+id/badge_target"
                android:text="@string/click_me"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"/>

        </com.cookizz.badgelib.BadgeRelativeLayout>

  2. In your `onCreate()` method, create a badge attached on `@id/badge_target`.

        // create a normal figure badge attached on your target
        BadgeManager manager = (BadgeManager) findViewById(R.id.badge_layout);
        FigureBadge badge = 
                manager.createFigureBadge(R.id.badge_target, FigureStyleNormal.class);
        
        // control your badge independent from `View` hierarchy
        badge.show();
        badge.setFigure(45);
    
  3. Once you obtained your badge reference and called its `show()` method, the following control accesses are now available for you.

        void badge.hide();
        boolean isShown();
        
        // Detach a badge from its target view, then the badge reference are disabled forever.
        void badge.detach();
        // Detect whether your badge is still available.
        boolean badge.isAttached();
        
        // A badge typed "Figure" has an access to set its figure.
        void badge.setFigure(100);
        int badge.getFigure();
        
        // Get the style info of your badge.
        BadgeStyle getStyle();
