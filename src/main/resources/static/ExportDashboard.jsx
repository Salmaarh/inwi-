import React from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Download, FileText, Table, FileBarChart } from "lucide-react";
import { motion } from "framer-motion";

const ExportDashboard = () => {
const formats = [
{
label: "CSV",
icon: <Table className="w-6 h-6 text-white" />,
url: "/api/reports/export/csv",
color: "bg-pink-600"
},
{
label: "Excel",
icon: <FileBarChart className="w-6 h-6 text-white" />,
url: "/api/reports/export/excel",
color: "bg-purple-700"
},
{
label: "PDF",
icon: <FileText className="w-6 h-6 text-white" />,
url: "/api/reports/export/pdf",
color: "bg-indigo-800"
}
];

return (
<div className="min-h-screen bg-gradient-to-br from-[#efefff] to-[#f6f6ff] flex flex-col items-center justify-center p-6">
    <motion.h1
            initial={{ opacity: 0, y: -30 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            className="text-4xl font-bold text-[#8a2be2] mb-10"
    >
        🌟 Tableau de bord INWI – Exportation des Rapports
    </motion.h1>

    <div className="grid grid-cols-1 md:grid-cols-3 gap-8 w-full max-w-5xl">
        {formats.map((format, index) => (
        <motion.div
                key={format.label}
                initial={{ opacity: 0, y: 40 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: index * 0.2 }}
        >
            <Card className={`shadow-xl ${format.color} text-white rounded-2xl`}>
                <CardContent className="flex flex-col items-center justify-center p-6">
                    <div className="mb-4">{format.icon}</div>
                    <h2 className="text-xl font-semibold mb-4">Exporter en {format.label}</h2>
                    <a href={format.url}>
                        <Button className="bg-white text-black hover:bg-gray-100 rounded-xl">
                            <Download className="mr-2 w-4 h-4" /> Télécharger
                        </Button>
                    </a>
                </CardContent>
            </Card>
        </motion.div>
        ))}
    </div>
</div>
);
};

export default ExportDashboard;
