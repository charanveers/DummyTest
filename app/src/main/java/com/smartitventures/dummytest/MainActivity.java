package com.smartitventures.dummytest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

public class MainActivity extends AppCompatActivity{
    private CardForm cardForm;
    private Button checkout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardForm =  findViewById(R.id.bt_card_form);
        checkout_button =  findViewById(R.id.checkout_card);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .actionLabel("Checkout")
                .setup(this);
        checkout_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (cardForm.isValid()){
                    getStripeToken(cardForm.getCardNumber(), cardForm.getExpirationMonth(), cardForm.getExpirationYear(), cardForm.getCvv());
                } else {
                    cardForm.validate();
                }
            }
        });
        cardForm.setOnCardFormSubmitListener(new OnCardFormSubmitListener(){
            @Override
            public void onCardFormSubmit() {
                if (cardForm.isValid()) {
                    getStripeToken(cardForm.getCardNumber(), cardForm.getExpirationMonth(), cardForm.getExpirationYear(), cardForm.getCvv());
                }else{
                    cardForm.validate();
                }
            }
        });
    }
    private void getStripeToken(String cardNumber, String expirationMonth, String expirationYear, String cvv){
        Card card = new Card(cardNumber, Integer.parseInt(expirationMonth), Integer.parseInt(expirationYear), cvv);
        Stripe stripe = null;
        try {
            stripe = new Stripe(MainActivity.this,"pk_test_U48DSqZSSvywgHrsjPDmpDtA00KjbCvn2q");
            stripe.createToken(
                    card,
                    new TokenCallback(){
                        public void onSuccess(Token token){
                            Toast.makeText(MainActivity.this,"charan :"+ token.getId(), Toast.LENGTH_SHORT).show();
                            // sendToServer(token.getId());
                        }
                        public void onError(Exception error){
                            // Show localized error message
                            Toast.makeText(MainActivity.this,
                                    error.getLocalizedMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
