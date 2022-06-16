import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ComboBoxEditor;

class AutoCompleteComboBox extends JComboBox {
    public int caretPos = 0;
    public JTextField tfield = null;
    private ArrayList<String> keywordList;
    
//----------------------------------------------------------------------------
    public AutoCompleteComboBox(String[] list) {
        super(list);
        setEditor(new BasicComboBoxEditor());
        setEditable(true);
    }
//----------------------------------------------------------------------------
    public void setModel(String[] array){
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(array);
        super.setModel(model);
    }
//----------------------------------------------------------------------------
    @Override
    public void setSelectedIndex(int index) {
        super.setSelectedIndex(index);
        tfield.setText(getItemAt(index).toString());
        tfield.setSelectionEnd(caretPos + tfield.getText().length());
        tfield.moveCaretPosition(caretPos);
    }
//----------------------------------------------------------------------------
    public void resetField(){
        tfield.setText("");
    }
//----------------------------------------------------------------------------
    @Override
    public void setEditor(ComboBoxEditor editor) {
        super.setEditor(editor);
        if(editor.getEditorComponent() instanceof JTextField) {
            tfield = (JTextField) editor.getEditorComponent();
            tfield.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent ke) {
                    char key = ke.getKeyChar();
                    if (!(Character.isLetterOrDigit(key) || Character.isSpaceChar(key) )) return;
                    caretPos = tfield.getCaretPosition();
                    String text="";
                    try {
                        text = tfield.getText(0, caretPos);
                    } catch (javax.swing.text.BadLocationException e) {
                        e.printStackTrace();
                    }
                    for (int i=0; i < getItemCount(); i++) {
                        String element = (String) getItemAt(i);
                        element = element.toLowerCase();
                        text = text.toLowerCase();
                        if (element.startsWith(text)) {
                            setSelectedIndex(i);
                            return;
                        }
                    }
                }
            });
        }
    }
}