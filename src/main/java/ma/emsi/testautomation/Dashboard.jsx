import React, { useRef } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Upload, ShieldCheck, Globe, Settings } from "lucide-react";

const Dashboard = () => {
    const fileInputRef = useRef<HTMLInputElement>(null);

    const handleFileUpload = async (e) => {

        const file = e.target.files?.[0];
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await fetch("/api/upload-numeros", {
                method: "POST",
                body: formData,
            });

            const result = await response.text();
            alert(result);
        } catch (err) {
            alert("Échec de l'upload !");
        }
    };

    const triggerFileInput = () => {
        fileInputRef.current?.click();
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-[#B17FC0] to-[#6A3F7A] p-6 text-white">
            <header className="flex justify-between items-center mb-8">
                <h1 className="text-3xl font-bold">Admin Dashboard</h1>
                <img src="inwi logo.png" alt="Logo" className="h-12" />
            </header>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {/* Upload Numéros */}
                <Card className="bg-[#8543A7] shadow-xl">
                    <CardContent className="p-6">
                        <div className="flex items-center space-x-4">
                            <Upload className="w-8 h-8" />
                            <div>
                                <h2 className="text-xl font-semibold">Upload Numéros</h2>
                                <p>Injecter automatiquement via un fichier</p>
                            </div>
                        </div>
                        <Button
                            className="mt-4 bg-white text-[#6A3F7A] hover:bg-gray-200"
                            onClick={triggerFileInput}
                        >
                            Uploader
                        </Button>
                        <input
                            type="file"
                            accept=".txt,.csv"
                            ref={fileInputRef}
                            onChange={handleFileUpload}
                            className="hidden"
                        />
                    </CardContent>
                </Card>

                {/* Sécurité */}
                <Card className="bg-[#8543A7] shadow-xl">
                    <CardContent className="p-6">
                        <div className="flex items-center space-x-4">
                            <ShieldCheck className="w-8 h-8" />
                            <div>
                                <h2 className="text-xl font-semibold">Sécurité</h2>
                                <p>Gérer les accès et les rôles</p>
                            </div>
                        </div>
                        <Button className="mt-4 bg-white text-[#6A3F7A] hover:bg-gray-200">Gérer la sécurité</Button>
                    </CardContent>
                </Card>

                {/* Web Services */}
                <Card className="bg-[#8543A7] shadow-xl">
                    <CardContent className="p-6">
                        <div className="flex items-center space-x-4">
                            <Globe className="w-8 h-8" />
                            <div>
                                <h2 className="text-xl font-semibold">Web Services</h2>
                                <p>Ajouter / Gérer les endpoints dynamiquement</p>
                            </div>
                        </div>
                        <Button className="mt-4 bg-white text-[#6A3F7A] hover:bg-gray-200">Configurer</Button>
                    </CardContent>
                </Card>

                {/* Paramètres */}
                <Card className="bg-[#8543A7] shadow-xl">
                    <CardContent className="p-6">
                        <div className="flex items-center space-x-4">
                            <Settings className="w-8 h-8" />
                            <div>
                                <h2 className="text-xl font-semibold">Paramètres Avancés</h2>
                                <p>Gestion des paramètres système</p>
                            </div>
                        </div>
                        <Button className="mt-4 bg-white text-[#6A3F7A] hover:bg-gray-200">Ouvrir</Button>
                    </CardContent>
                </Card>
            </div>
        </div>
    );
};

export default Dashboard;
