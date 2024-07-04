#include <jni.h>
#include <string>

extern "C" {
JNIEXPORT jstring JNICALL
Java_br_com_lg_MyWay_newgentemobile_configuracoes_seguranca_ArmazenamentoSeguroNDK_getIdentityPoolId(JNIEnv *env, jobject /*this*/) {
    std::string identityPoolId = "us-east-1:abdc4ea9-328e-4797-9330-4db89ad59213";
    return env->NewStringUTF(identityPoolId.c_str());
}
}
