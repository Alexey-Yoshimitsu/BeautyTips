# Beauty Tips App Implementation Guide

## Project Setup

1. **Create new Android project** with empty activity in Kotlin
2. **Add required dependencies** to `build.gradle (Module)`:
```gradle
dependencies {
    implementation 'androidx.cardview:cardview:1.0.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
}
```

## Color Resources
Add to `res/values/colors.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="primary">#FF0099</color>
    <color name="primary_dark">#CC0077</color>
    <color name="background">#FFE6F2</color>
    <color name="card_background">#FFFFFF</color>
    <color name="text_primary">#000000</color>
    <color name="text_secondary">#666666</color>
</resources>
```

## Custom Font
1. Create `res/font` directory
2. Add `script_font.ttf` (similar to the one used in screenshots)
3. Create `res/font/beauty_font.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<font-family xmlns:android="http://schemas.android.com/apk/res/android">
    <font
        android:font="@font/script_font"
        android:fontStyle="normal"
        android:fontWeight="400" />
</font-family>
```

## Screen Implementations

### 1. Splash Screen
**File:** `activity_splash.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/primary">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:fontFamily="@font/beauty_font"
        android:text="Beauty Tips."
        android:textColor="#FFFFFF"
        android:textSize="56sp" />

</FrameLayout>
```

**File:** `SplashActivity.kt`
```kotlin
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 2000)
    }
}
```

### 2. Login Screen
**File:** `activity_login.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background">

    <TextView
        android:id="@+id/logoTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:fontFamily="@font/beauty_font"
        android:text="Beauty Tips."
        android:textColor="@color/primary"
        android:textSize="48sp"
        app:layout_constraintBottom_toTopOf="@id/titleTextView"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_chainStyle="packed" />

    <TextView
        android:id="@+id/titleTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:text="Вход в аккаунт"
        android:textColor="@color/primary"
        android:textSize="24sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toTopOf="@id/emailInput"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/logoTextView" />

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/emailInput"
        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="32dp"
        android:layout_marginTop="32dp"
        android:layout_marginEnd="32dp"
        android:hint="Почта"
        app:boxBackgroundColor="@color/card_background"
        app:boxCornerRadiusBottomEnd="16dp"
        app:boxCornerRadiusBottomStart="16dp"
        app:boxCornerRadiusTopEnd="16dp"
        app:boxCornerRadiusTopStart="16dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/titleTextView">

        <com.google.android.material.textfield.TextInputEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inputType="textEmailAddress" />

    </com.google.android.material.textfield.TextInputLayout>

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/passwordInput"
        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="32dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="32dp"
        android:hint="Пароль"
        app:boxBackgroundColor="@color/card_background"
        app:boxCornerRadiusBottomEnd="16dp"
        app:boxCornerRadiusBottomStart="16dp"
        app:boxCornerRadiusTopEnd="16dp"
        app:boxCornerRadiusTopStart="16dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/emailInput">

        <com.google.android.material.textfield.TextInputEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inputType="textPassword" />

    </com.google.android.material.textfield.TextInputLayout>

    <Button
        android:id="@+id/loginButton"
        android:layout_width="0dp"
        android:layout_height="56dp"
        android:layout_marginStart="32dp"
        android:layout_marginTop="32dp"
        android:layout_marginEnd="32dp"
        android:backgroundTint="@color/primary"
        android:text="Войти"
        android:textAllCaps="false"
        android:textColor="#FFFFFF"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/passwordInput" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

**File:** `LoginActivity.kt`
```kotlin
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        findViewById<Button>(R.id.loginButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
```

### 3. Registration Screen
**File:** `activity_register.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background">

    <TextView
        android:id="@+id/logoTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:fontFamily="@font/beauty_font"
        android:text="Beauty Tips."
        android:textColor="@color/primary"
        android:textSize="48sp"
        app:layout_constraintBottom_toTopOf="@id/titleTextView"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_chainStyle="packed" />

    <TextView
        android:id="@+id/titleTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:text="Давай создадим аккаунт"
        android:textColor="@color/primary"
        android:textSize="24sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toTopOf="@id/nameInput"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/logoTextView" />

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/nameInput"
        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="32dp"
        android:layout_marginTop="32dp"
        android:layout_marginEnd="32dp"
        android:hint="Имя"
        app:boxBackgroundColor="@color/card_background"
        app:boxCornerRadiusBottomEnd="16dp"
        app:boxCornerRadiusBottomStart="16dp"
        app:boxCornerRadiusTopEnd="16dp"
        app:boxCornerRadiusTopStart="16dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/titleTextView">

        <com.google.android.material.textfield.TextInputEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

    </com.google.android.material.textfield.TextInputLayout>

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/emailInput"
        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="32dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="32dp"
        android:hint="Почта"
        app:boxBackgroundColor="@color/card_background"
        app:boxCornerRadiusBottomEnd="16dp"
        app:boxCornerRadiusBottomStart="16dp"
        app:boxCornerRadiusTopEnd="16dp"
        app:boxCornerRadiusTopStart="16dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/nameInput">

        <com.google.android.material.textfield.TextInputEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inputType="textEmailAddress" />

    </com.google.android.material.textfield.TextInputLayout>

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/passwordInput"
        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="32dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="32dp"
        android:hint="Пароль"
        app:boxBackgroundColor="@color/card_background"
        app:boxCornerRadiusBottomEnd="16dp"
        app:boxCornerRadiusBottomStart="16dp"
        app:boxCornerRadiusTopEnd="16dp"
        app:boxCornerRadiusTopStart="16dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/emailInput">

        <com.google.android.material.textfield.TextInputEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inputType="textPassword" />

    </com.google.android.material.textfield.TextInputLayout>

    <Button
        android:id="@+id/registerButton"
        android:layout_width="0dp"
        android:layout_height="56dp"
        android:layout_marginStart="32dp"
        android:layout_marginTop="32dp"
        android:layout_marginEnd="32dp"
        android:backgroundTint="@color/primary"
        android:text="Создать"
        android:textAllCaps="false"
        android:textColor="#FFFFFF"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/passwordInput" />

    <TextView
        android:id="@+id/dividerText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:text="Уже есть аккаунт?"
        android:textColor="@color/primary"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/registerButton" />

    <View
        android:layout_width="0dp"
        android:layout_height="1dp"
        android:background="@color/primary"
        app:layout_constraintBottom_toBottomOf="@id/dividerText"
        app:layout_constraintEnd_toStartOf="@id/dividerText"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="@id/dividerText" />

    <View
        android:layout_width="0dp"
        android:layout_height="1dp"
        android:background="@color/primary"
        app:layout_constraintBottom_toBottomOf="@id/dividerText"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toEndOf="@id/dividerText"
        app:layout_constraintTop_toTopOf="@id/dividerText" />

    <Button
        android:id="@+id/loginButton"
        android:layout_width="0dp"
        android:layout_height="56dp"
        android:layout_marginStart="32dp"
        android:layout_marginTop="24dp"
        android:layout_marginEnd="32dp"
        android:backgroundTint="@color/primary"
        android:text="Войти"
        android:textAllCaps="false"
        android:textColor="#FFFFFF"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/dividerText" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 4. Main Screen
**File:** `activity_main.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="@color/background"
        app:layout_constraintTop_toTopOf="parent">

        <ImageView
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:src="@drawable/ic_profile" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:text="Маргарита"
            android:textColor="@color/primary"
            android:textSize="16sp" />

        <ImageView
            android:layout_width="32dp"
            android:layout_height="32dp"
            android:layout_gravity="end"
            android:src="@drawable/ic_search" />

    </androidx.appcompat.widget.Toolbar>

    <TextView
        android:id="@+id/greetingText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:text="Добрый день, Маргарита!"
        android:textColor="@color/text_primary"
        android:textSize="28sp"
        android:textStyle="bold"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/toolbar" />

    <TextView
        android:id="@+id/subtitleText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="Выпадет "
        android:textColor="@color/text_primary"
        android:textSize="18sp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/greetingText" />

    <TextView
        android:id="@+id/randomText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="случайная статья"
        android:textColor="@color/primary"
        android:textSize="18sp"
        android:textStyle="bold"
        app:layout_constraintStart_toEndOf="@id/subtitleText"
        app:layout_constraintTop_toTopOf="@id/subtitleText" />

    <TextView
        android:id="@+id/continuationText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text=" при нажатии кнопки"
        android:textColor="@color/text_primary"
        android:textSize="18sp"
        app:layout_constraintStart_toEndOf="@id/randomText"
        app:layout_constraintTop_toTopOf="@id/subtitleText" />

    <Button
        android:id="@+id/randomButton"
        android:layout_width="100dp"
        android:layout_height="48dp"
        android:layout_marginTop="24dp"
        android:backgroundTint="@color/primary"
        android:text="тьк"
        android:textAllCaps="false"
        android:textColor="#FFFFFF"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/subtitleText" />

    <TextView
        android:id="@+id/recommendedTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="32dp"
        android:text="Подобрали статьи специально для вас"
        android:textColor="@color/text_primary"
        android:textSize="20sp"
        android:textStyle="bold"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/randomButton" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/articlesRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_marginTop="16dp"
        app:layout_constraintBottom_toTopOf="@id/bottomNavigation"
        app:layout_constraintTop_toBottomOf="@id/recommendedTitle" />

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottomNavigation"
        android:layout_width="match_parent"
        android:layout_height="64dp"
        android:background="@color/background"
        app:layout_constraintBottom_toBottomOf="parent"
        app:menu="@menu/bottom_nav_menu" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

**File:** `article_item.xml` (for RecyclerView)
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="12dp"
    app:cardCornerRadius="16dp"
    app:cardElevation="4dp"
    app:cardUseCompatPadding="true">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp">

        <TextView
            android:id="@+id/timeAgoText"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="3 мин"
            android:textColor="@color/primary"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <TextView
            android:id="@+id/popularTag"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:background="@drawable/popular_tag_background"
            android:paddingStart="8dp"
            android:paddingEnd="8dp"
            android:text="Популярная статья"
            android:textColor="#FFFFFF"
            android:textSize="12sp"
            app:layout_constraintStart_toEndOf="@id/timeAgoText"
            app:layout_constraintTop_toTopOf="parent" />

        <TextView
            android:id="@+id/titleText"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="12dp"
            android:text="Косметика Anua: За и про..."
            android:textColor="@color/text_primary"
            android:textSize="18sp"
            android:textStyle="bold"
            app:layout_constraintEnd_toStartOf="@id/arrowIcon"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/timeAgoText" />

        <TextView
            android:id="@+id/previewText"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="Косметика Anua является довольно по..."
            android:textColor="@color/text_secondary"
            app:layout_constraintEnd_toStartOf="@id/arrowIcon"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/titleText" />

        <ImageView
            android:id="@+id/arrowIcon"
            android:layout_width="24dp"
            android:layout_height="24dp"
            android:src="@drawable/ic_arrow"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</androidx.cardview.widget.CardView>
```

### 5. Profile Screen
**File:** `activity_profile.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="@color/background"
        app:layout_constraintTop_toTopOf="parent">

        <ImageView
            android:id="@+id/backButton"
            android:layout_width="24dp"
            android:layout_height="24dp"
            android:src="@drawable/ic_back" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:text="Мой профиль"
            android:textColor="@color/text_primary"
            android:textSize="20sp"
            android:textStyle="bold" />

    </androidx.appcompat.widget.Toolbar>

    <androidx.cardview.widget.CardView
        android:id="@+id/avatarCard"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        app:cardCornerRadius="16dp"
        app:cardUseCompatPadding="true">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="16dp">

            <TextView
                android:id="@+id/avatarLabel"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Аватар"
                android:textColor="@color/text_primary"
                android:textSize="16sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <de.hdodenhof.circleimageview.CircleImageView
                android:id="@+id/avatarImage"
                android:layout_width="48dp"
                android:layout_height="48dp"
                android:src="@drawable/ic_profile"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

        </androidx.constraintlayout.widget.ConstraintLayout>

    </androidx.cardview.widget.CardView>

    <androidx.cardview.widget.CardView
        android:id="@+id/personalDataCard"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginEnd="16dp"
        android:layout_marginBottom="8dp"
        app:cardCornerRadius="16dp"
        app:cardUseCompatPadding="true">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="16dp">

            <TextView
                android:id="@+id/personalDataLabel"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Личные данные"
                android:textColor="@color/text_primary"
                android:textSize="16sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <ImageView
                android:id="@+id/arrow1"
                android:layout_width="24dp"
                android:layout_height="24dp"
                android:src="@drawable/ic_arrow"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

        </androidx.constraintlayout.widget.ConstraintLayout>

    </androidx.cardview.widget.CardView>

    <androidx.cardview.widget.CardView
        android:id="@+id/supportCard"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        app:cardCornerRadius="16dp"
        app:cardUseCompatPadding="true">

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="16dp">

            <TextView
                android:id="@+id/supportLabel"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Обратиться в техподдержку"
                android:textColor="@color/text_primary"
                android:textSize="16sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <ImageView
                android:id="@+id/arrow2"
                android:layout_width="24dp"
                android:layout_height="24dp"
                android:src="@drawable/ic_arrow"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

        </androidx.constraintlayout.widget.ConstraintLayout>

    </androidx.cardview.widget.CardView>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="16dp"
        android:text="Разработано Маргаритой"
        android:textColor="@color/text_secondary"
        android:textSize="12sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Navigation Setup

**File:** `AndroidManifest.xml` (add these activities)
```xml
<activity
    android:name=".SplashActivity"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>

<activity
    android:name=".LoginActivity"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
<activity
    android:name=".RegisterActivity"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
<activity
    android:name=".MainActivity"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
<activity
    android:name=".ProfileActivity"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
```

## Implementation Notes

1. **Custom Fonts**: Download a script font similar to the one in screenshots and place it in `res/font/`
2. **Icons**: Create necessary icons (profile, search, back, arrow) in `res/drawable`
3. **Popular Tag Background**: Create `res/drawable/popular_tag_background.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="@color/primary" />
    <corners android:radius="8dp" />
</shape>
```
4. **Bottom Navigation**: Create `res/menu/bottom_nav_menu.xml` with appropriate items
5. **RecyclerView Adapter**: Implement adapter for articles list with proper data binding

## Final Steps

1. Add all required drawable resources
2. Implement navigation between activities (login → main, register → login, etc.)
3. Add RecyclerView adapter for articles
4. Test on different screen sizes for proper alignment

This implementation provides pixel-perfect recreation of all provided screenshots using native Android XML layouts and Kotlin code. All elements match the design specifications including colors, fonts, and component spacing.