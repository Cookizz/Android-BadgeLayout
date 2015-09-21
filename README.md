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
    
  3. Once you obtained a badge reference and called its `show()` method, the following control accesses are now available for you.

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

## Design
  
  As the example above, we used the built-in `FigureStyleNormal` to define a "Figure" badge style. You may design a custom style as you want.
  
  1. Create a subclass of `FigureStyleTemplate`.
  
        public class MyFigureStyle extends FigureStyleTemplate {}

  2. Implement methods that are used to define the styles.

        @Override
        public Point getReferencedScreenResolution() {
            // you must point out which screen resolution your design is based on
            // the x value indicates the width pixels of the screen, y, of course, the height
            return new Point(750, 1334);
        }
    
        @Override
        public Point getGravity() {
            // 
            return new Point(1, -1);
        }
    
        @Override
        public Point getOffset() {
            return new Point(10, -10);
        }
        
        @Override
        public int getTextSize() {
            //
            return 24;
        }
    
        @Override
        public int getTextColor(Context context) {
            return Color.rgb(255, 255, 255);
        }
    
        @Override
        public Typeface getTypeface(Context context) {
            return null;
        }
    
        @Override
        public int getBackgroundColor(Context context) {
            return Color.rgb(238, 37, 45);
        }
    
        @Override
        public int getTerminalRadius() {
            return 18;
        }
    
        @Override
        public int getWidth(int figure) {
            if(figure < 0) {
                return 0;
            }
            final int divideBy10 = figure / 10;
            if(divideBy10 == 0) {
                return 36;
            }
            else if(divideBy10 < 10) {
                return 48;
            }
            else {
                return 62;
            }
        }
    
        @Override
        public String getText(int figure) {
            String text;
            if(figure > 99) {
                text = "99+";
            }
            else {
                text = String.valueOf(figure);
            }
            return text;
        }
    
        @Override
        public boolean isVisible(int figure) {
            return figure > 0;
        }
    
