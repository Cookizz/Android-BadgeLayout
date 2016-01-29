# Android-BadgeLayout
BadgeLayout provides an easier way of designing, creating and controlling badges within your Android apps. You pay no attention to any View issues during your development.

## Requirement
  
  1. Android API 9 or higher.
  2. Add [badge.jar](https://github.com/Cookizz/Android-BadgeLayout/blob/master/release/badge.jar) to your file dependencies.
  
## Usage

  1. Include `com.cookizz.badgelib.BadgeRelativeLayout` or `com.cookizz.badgelib.BadgeFrameLayout` in your layout (Here I take `BadgeRelativeLayout` for example). Wrap your target views that you intend to put badges on within `BadgeRelativeLayout`.

        <com.cookizz.badgelib.BadgeRelativeLayout
            android:id="@+id/badge_manager"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
            
            <TextView
                android:id="@+id/text_1"
                android:text="@string/dot"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="20dp"
                android:padding="20dp"
                android:background="@color/trans_black"/>

            <TextView
                android:id="@+id/text_2"
                android:text="@string/figure"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_toRightOf="@+id/text_1"
                android:layout_margin="20dp"
                android:padding="20dp"
                android:background="@color/trans_black"/>
                
        </com.cookizz.badgelib.BadgeRelativeLayout>

  In no matter which view hierarchy in `BdadeRelativeLayout` will it be OK for your targets to put except ScrollView and ListView, which may be supported later on. Instead, you can include your `BadgeRelativeLayout` into ListView's item layout or inside the `ScrollView`'s layout for an equivalent result.

  2. In your `onCreate()` method, create a DotBadge on `@id/text_1` and a FigureBadge on `@id/text_2` from your `BadgeRelativeLayout` using their default styles.

        BadgeManager manager = (BadgeManager) findViewById(R.id.badge_layout);
        
        // create a dot badge on @id/text_1
        manager.createDotBadge(R.id.text_1, DotStyleNormal.class);
                .show();
        
        // create a figure badge on @id/text_2 and set a figure
        manager.createFigureBadge(R.id.text_2, FigureStyleNormal.class);
                .setFigure(45)
                .show();

  Then here comes the result: 
  
  ![code effect](https://github.com/Cookizz/Android-BadgeLayout/blob/master/badgedemo/src/main/res/raw/dotandfigure.png)
    
  3. Once you obtained a badge reference and called its `show()` method, the following control accesses to DotBadge/FigureBadge are now available for you.

        void hide();
        boolean isShown();
        void detach();
        boolean isAttached();
        BadgeStyle getStyle();

        /**
         * FigureBadge exclusive methods
         */
        void setFigure(int);
        int getFigure();

## Design your badge style
  Quite easy! Take `FigureBadge` as an example...
  
  1. Create a subclass of `FigureStyleTemplate` and implement your design.
  
        public class MyFigureStyle extends FigureStyleTemplate {
        
            /** 
             * You must point out which screen resolution your design is based on.
             * The base class will automatically complete screen adaption in runtime.
             * Point's x value indicates width pixels of the screen,
             * y, of course, the height.
             */
            @Override
            public Point getReferencedScreenResolution() { // your design }
        
            /**
             * Tell the base class which direction your badge goes
             * when it's going to be attached on the target view.
             * Point's x value indicates the x direction, y, of course, the y direction.
             * For instance, (1, -1) shows that it will be put adjacent
             * to the right-top corner of the target view.
             * (0, 0) shows that it will be put at the center.
             */
            @Override
            public Point getGravity() { // your design }
        
            /**
             * After the gravity has been set, you can set an extra offset the badge will go.
             * Point's x value indicates the x offset, y, of course, the y offset.
             */
            @Override
            public Point getOffset() { // your design }
            
            /**
             * Point out the size of figure text.
             */
            @Override
            public int getTextSize() { // your design }
        
            /**
             * Point out the color of figure text.
             */
            @Override
            public int getTextColor(Context context) { // your design }
        
            /**
             * Point out the typeface of figure text.
             */
            @Override
            public Typeface getTypeface(Context context) { // your design }
        
            /**
             * Point out the background of the badge's background.
             */
            @Override
            public int getBackgroundColor(Context context) { // your design }
        
            /**
             * Point out the terminal radius when the badge displays as a fully round rect.
             */
            @Override
            public int getTerminalRadius() { // your design }
        
            /**
             * Point out the width relative to specified figure value.
             */
            @Override
            public int getWidth(int figure) { // your design }
        
            /**
             * Point out the intrinsic text matching the comming specified figure value.
             */
            @Override
            public String getText(int figure) { // your design }
        
            /**
             * Point out in which case the badge should be visible
             * relative to specified figure value.
             */
            @Override
            public boolean isVisible(int figure) { // your design }
        }
    
  2. Put your design into use.

        BadgeManager manager = (BadgeManager) findViewById(R.id.badge_layout);
        FigureBadge badge = manager.createFigureBadge(R.id.badge_target, MyFigureStyle.class);

## Limitations
  
  * Not supporting "any-text" badge so far, coming soon.
  
  * No touching event available on the badge, coming soon.
  
  * If you intend to put a badge into a list item or into a ScrollView, do not wrap the list or the ScrollView into BadgeRelativeLayout. Contrarily, put BadgeRelativeLayout into their View tree.

## Developed By

  * Cookizz - <com.cookizz@gmail.com>

## License

    Copyright 2015 Cookizz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
