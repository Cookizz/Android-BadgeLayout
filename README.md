# Android-BadgeLayout
Offers an easier way of designing, creating and controlling badges within your Android apps. You pay no attention to any View issues during your development.

![provided by designers from corp 21cn](https://github.com/Cookizz/Android-BadgeLayout/blob/master/badgedemo/src/main/res/raw/badgesample.png)

*(picture above is provided by designers from corp 21CN)*

## Usage
There are two types of Badge: `FigureBadge` and `DotBadge`. `FigureBadge` has just the same usage as `DotBadge`. Additionally, it has more features. So the following routine aims at `FigureBadge` and its default style `FigureStyleNormal` as an example.

  1. Include `BadgeRelativeLayout` in your view. Generally, it should be a container of those target views who you intend to put badges on.
  
  (no ListView/ScrollView should be inside the container, they may be supported later on)
  
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

        BadgeManager manager = (BadgeManager) findViewById(R.id.badge_layout);
        FigureBadge badge = manager.createFigureBadge(R.id.badge_target, FigureStyleNormal.class);
        
        // control your badge object(not a View) independent from View hierarchy
        badge.show();
        badge.setFigure(45);
        
  ![code effect](https://github.com/Cookizz/Android-BadgeLayout/blob/master/badgedemo/src/main/res/raw/addbadgeon.png)
    
  3. Once you obtained a badge reference and called its `show()` method, the following control accesses are now available for you.
        
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
  
  1. Create a subclass of `FigureStyleTemplate` and implement your design.
  
        public class MyFigureStyle extends FigureStyleTemplate {
        
            /** 
             * You must point out which screen resolution your design is based on.
             * The base class will automatically complete screen adaption in the runtime.
             * Point's x value indicates the width pixels of the screen,
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
             * Point out the intrinsic text relative to specified figure value.
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

  * Only RelativeLayout can be replaced by now, FrameLayout is coming soon.
  
  * Not supporting "any-text" badge so far, coming soon.
  
  * No touching event available on the badge, coming soon.
  
  * If you intend to put a badge on a list item or into a ScrollView, do not wrap the list or the ScrollView into BadgeRelativeLayout. Contrarily, put BadgeRelativeLayout into their View tree.

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
